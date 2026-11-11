package br.com.portfolio.pokedex.service;

import br.com.portfolio.pokedex.client.PokeApiClient;
import br.com.portfolio.pokedex.dto.PokemonResponse;
import br.com.portfolio.pokedex.exception.UpstreamServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

@Service
public class PokemonService {

    private final PokeApiClient client;

    public PokemonService(PokeApiClient client) {
        this.client = client;
    }

    public PokemonResponse buscar(String nameOrId) {
        if (nameOrId == null) {
            throw new IllegalArgumentException("Busca invalida.");
        }

        String termo = nameOrId.trim().toLowerCase(Locale.ROOT);

        if (!termo.matches("[a-z0-9-]{1,64}")) {
            throw new IllegalArgumentException("Busca invalida.");
        }

        JsonNode pokemon = client.buscar(termo);

        if (pokemon == null || pokemon.path("id").asInt() <= 0) {
            throw new UpstreamServiceException();
        }

        List<String> tipos = new ArrayList<>();

        for (JsonNode item : pokemon.path("types")) {
            String nome = item.path("type").path("name").asText();

            if (!nome.isBlank()) {
                tipos.add(nome);
            }
        }

        List<String> habilidades = new ArrayList<>();

        for (JsonNode item : pokemon.path("abilities")) {
            String nome = item.path("ability").path("name").asText();

            if (!nome.isBlank()) {
                habilidades.add(nome);
            }
        }

        JsonNode sprites = pokemon.path("sprites");

        String imagem = sprites.path("other")
            .path("official-artwork")
            .path("front_default")
            .asText();

        if (imagem == null || imagem.isBlank()) {
            imagem = sprites.path("front_default").asText();
        }

        return new PokemonResponse(
            pokemon.path("id").asInt(),
            pokemon.path("name").asText(),
            imagem,
            List.copyOf(tipos),
            pokemon.path("height").asInt() / 10.0,
            pokemon.path("weight").asInt() / 10.0,
            List.copyOf(habilidades)
        );
    }
}