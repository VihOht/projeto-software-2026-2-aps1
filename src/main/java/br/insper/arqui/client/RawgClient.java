package br.insper.arqui.client;

import br.insper.arqui.dto.RawgGamesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RawgClient {

    private final RestClient restClient;
    private final String apiKey;

    public RawgClient(
            RestClient.Builder restClientBuilder,
            @Value("${rawg.api.url}") String apiUrl,
            @Value("${rawg.api.key}") String apiKey
    ) {
        this.restClient = restClientBuilder
                .baseUrl(apiUrl)
                .build();

        this.apiKey = apiKey;
    }

    public RawgGamesResponse buscarJogos(String busca) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("key", apiKey)
                        .queryParam("search", busca)
                        .queryParam("page_size", 10)
                        .build())
                .retrieve()
                .body(RawgGamesResponse.class);
    }
}