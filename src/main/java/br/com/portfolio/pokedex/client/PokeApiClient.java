package br.com.portfolio.pokedex.client;

import br.com.portfolio.pokedex.exception.PokemonNotFoundException;
import br.com.portfolio.pokedex.exception.UpstreamServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.JsonNode;

@Component
public class PokeApiClient {

    private final RestClient restClient;

    public PokeApiClient(
            RestClient.Builder builder,
            @Value("${pokedex.api.base-url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public JsonNode buscar(String nameOrId) {
        try {
            return restClient.get()
                .uri("/pokemon/{nameOrId}", nameOrId)
                .retrieve()
                .body(JsonNode.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new PokemonNotFoundException();
        } catch (RestClientException exception) {
            throw new UpstreamServiceException(exception);
        }
    }
}