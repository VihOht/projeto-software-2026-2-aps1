package br.insper.arqui.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;

@Component
public class JogosClient {

    private final RestClient restClient;

    public JogosClient(
            RestClient.Builder restClientBuilder,
            @Value("${jogos.service.url}") String jogosServiceUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(jogosServiceUrl)
                .requestInterceptor((request, body, execution) -> {
                    var authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication instanceof JwtAuthenticationToken jwt && jwt.isAuthenticated()) {
                        request.getHeaders().setBearerAuth(jwt.getToken().getTokenValue());
                    }
                    return execution.execute(request, body);
                })
                .build();
    }

    public boolean jogoExiste(Long jogoId) {
        try {
            restClient.get()
                    .uri("/api/jogos/{id}", jogoId)
                    .retrieve()
                    .toBodilessEntity();

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}