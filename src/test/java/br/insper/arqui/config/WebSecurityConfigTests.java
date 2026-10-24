package br.insper.arqui.config;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Instant;
import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(WebSecurityConfigTests.TestConfig.class)
@TestPropertySource(locations = "classpath:application.properties")
class WebSecurityConfigTests {
    private static final String ISSUER = "https://dev-bpbgrel3zl2chj7s.us.auth0.com/";
    private static final String AUDIENCE = ISSUER + "api/v2/";
    private static final RSAKey KEY;
    private static final com.sun.net.httpserver.HttpServer JWKS;
    static {
        try {
            KEY = new RSAKeyGenerator(2048).keyID("test-key").generate();
            JWKS = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress("127.0.0.1", 0), 0);
            JWKS.createContext("/jwks", exchange -> {
                byte[] body = new com.nimbusds.jose.jwk.JWKSet(KEY.toPublicJWK()).toString()
                        .getBytes(java.nio.charset.StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, body.length);
                try (var output = exchange.getResponseBody()) { output.write(body); }
            });
            JWKS.start();
        } catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }

    @org.junit.jupiter.api.AfterAll
    static void stopJwks() { JWKS.stop(0); }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> "http://127.0.0.1:" + JWKS.getAddress().getPort() + "/jwks");
    }

    @Configuration
    @EnableWebMvc
    @Import({WebSecurityConfig.class, OAuth2ResourceServerAutoConfiguration.class, Probe.class})
    static class TestConfig {}

    @RestController
    static class Probe {
        @GetMapping("/security-probe") String get() { return "ok"; }
        @PostMapping("/security-probe") String post() { return "ok"; }
    }

    @Autowired WebApplicationContext context;
    MockMvc mvc;

    @BeforeEach void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test void rejectsMissingToken() throws Exception {
        mvc.perform(get("/security-probe")).andExpect(status().isUnauthorized());
    }

    @Test void acceptsValidTokenWithoutCreatingSession() throws Exception {
        mvc.perform(get("/security-probe").header("Authorization", "Bearer " + token(ISSUER, AUDIENCE, 300, KEY)))
                .andExpect(status().isOk()).andExpect(content().string("ok"))
                .andExpect(result -> org.junit.jupiter.api.Assertions.assertNull(result.getRequest().getSession(false)));
    }

    @Test void acceptsAuthenticatedPostWithoutCsrfCookie() throws Exception {
        mvc.perform(post("/security-probe").header("Authorization", "Bearer " + token(ISSUER, AUDIENCE, 300, KEY)))
                .andExpect(status().isOk());
    }

    @Test void rejectsWrongAudienceIssuerExpiredAndForgedTokens() throws Exception {
        String[] tokens = {
                token(ISSUER, "another-api", 300, KEY),
                token("https://another-issuer/", AUDIENCE, 300, KEY),
                token(ISSUER, AUDIENCE, -300, KEY),
                token(ISSUER, AUDIENCE, 300, new RSAKeyGenerator(2048).generate()),
                "malformed-token"
        };
        for (String token : tokens) {
            mvc.perform(get("/security-probe").header("Authorization", "Bearer " + token))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test void allowsFrontendPreflightWithoutToken() throws Exception {
        mvc.perform(options("/security-probe")
                        .header("Origin", "http://localhost:5500")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5500"));
    }

    @Test void rejectsUnknownCorsOrigin() throws Exception {
        mvc.perform(options("/security-probe")
                        .header("Origin", "https://untrusted.example")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }

    private String token(String issuer, String audience, long expiresIn, RSAKey key) throws Exception {
        var claims = new JWTClaimsSet.Builder().issuer(issuer).audience(audience).subject("auth0|test")
                .issueTime(Date.from(Instant.now().minusSeconds(600)))
                .expirationTime(Date.from(Instant.now().plusSeconds(expiresIn))).build();
        var jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID("test-key").type(JOSEObjectType.JWT).build(), claims);
        jwt.sign(new RSASSASigner(key));
        return jwt.serialize();
    }
}
