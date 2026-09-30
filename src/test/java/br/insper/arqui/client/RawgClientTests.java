package br.insper.arqui.client;

import br.insper.arqui.dto.RawgGamesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class RawgClientTests {

    @Test
    void deveBuscarJogosNaRawg() {

        RestClient.Builder builder = RestClient.builder();

        MockRestServiceServer server =
                MockRestServiceServer.bindTo(builder).build();

        RawgClient rawgClient = new RawgClient(
                builder,
                "https://api.rawg.io/api",
                "chave-teste"
        );

        String json = """
                {
                  "count": 1,
                  "results": [
                    {
                      "id": 22509,
                      "name": "Minecraft",
                      "released": "2009-05-10",
                      "rating": 4.43,
                      "background_image": "imagem.jpg",
                      "genres": [
                        {
                          "id": 4,
                          "name": "Action"
                        }
                      ],
                      "platforms": [
                        {
                          "platform": {
                            "id": 4,
                            "name": "PC",
                            "slug": "pc"
                          }
                        }
                      ]
                    }
                  ]
                }
                """;

        server.expect(requestTo(
                        "https://api.rawg.io/api/games?key=chave-teste&search=minecraft&page_size=10"
                ))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        RawgGamesResponse response =
                rawgClient.buscarJogos("minecraft");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.count());
        Assertions.assertEquals(1, response.results().size());
        Assertions.assertEquals(
                "Minecraft",
                response.results().getFirst().name()
        );
        Assertions.assertEquals(
                4.43,
                response.results().getFirst().rating()
        );
        Assertions.assertEquals(
                "Action",
                response.results().getFirst().genres().getFirst().name()
        );
        Assertions.assertEquals(
                "PC",
                response.results()
                        .getFirst()
                        .platforms()
                        .getFirst()
                        .platform()
                        .name()
        );

        server.verify();
    }
}