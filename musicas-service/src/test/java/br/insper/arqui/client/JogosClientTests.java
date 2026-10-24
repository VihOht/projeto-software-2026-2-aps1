package br.insper.arqui.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class JogosClientTests {
    @AfterEach void clearContext() { SecurityContextHolder.clearContext(); }

    @Test void forwardsAuthenticatedTokenToGamesService() {
        var builder = RestClient.builder();
        var server = MockRestServiceServer.bindTo(builder).build();
        var client = new JogosClient(builder, "http://jogos-service:8080");
        var jwt = Jwt.withTokenValue("user-access-token").header("alg", "RS256")
                .subject("auth0|test").build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt, java.util.List.of()));
        server.expect(requestTo("http://jogos-service:8080/api/jogos/42"))
                .andExpect(header("Authorization", "Bearer user-access-token"))
                .andRespond(withSuccess());
        assertTrue(client.jogoExiste(42L));
        server.verify();
    }

    @Test void doesNotReuseTokenAfterRequestContextIsCleared() {
        var builder = RestClient.builder();
        var server = MockRestServiceServer.bindTo(builder).build();
        var client = new JogosClient(builder, "http://jogos-service:8080");
        var jwt = Jwt.withTokenValue("previous-token").header("alg", "RS256").subject("user").build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt, java.util.List.of()));
        server.expect(requestTo("http://jogos-service:8080/api/jogos/1"))
                .andExpect(header("Authorization", "Bearer previous-token")).andRespond(withSuccess());
        server.expect(requestTo("http://jogos-service:8080/api/jogos/2"))
                .andExpect(headerDoesNotExist("Authorization")).andRespond(withSuccess());
        assertTrue(client.jogoExiste(1L));
        SecurityContextHolder.clearContext();
        assertTrue(client.jogoExiste(2L));
        server.verify();
    }
}
