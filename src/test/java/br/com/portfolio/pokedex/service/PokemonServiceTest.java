package br.com.portfolio.pokedex.service;

import br.com.portfolio.pokedex.client.PokeApiClient;
import br.com.portfolio.pokedex.dto.PokemonResponse;
import br.com.portfolio.pokedex.exception.PokemonNotFoundException;
import br.com.portfolio.pokedex.exception.UpstreamServiceException;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonServiceTest {

    private final PokeApiClient client = mock(PokeApiClient.class);
    private final PokemonService service = new PokemonService(client);

    private JsonNode pokemon() {
        return JsonMapper.builder().build().readTree("""
            {
              "id": 25,
              "name": "pikachu",
              "height": 4,
              "weight": 60,
              "sprites": {
                "front_default": "https://example.com/basic.png",
                "other": {
                  "official-artwork": {
                    "front_default": "https://example.com/pikachu.png"
                  }
                }
              },
              "types": [
                {"type": {"name": "electric"}}
              ],
              "abilities": [
                {"ability": {"name": "static"}},
                {"ability": {"name": "lightning-rod"}}
              ]
            }
            """);
    }

    @Test
    void mapeiaDadosEConverteUnidades() {
        when(client.buscar("pikachu")).thenReturn(pokemon());

        PokemonResponse resposta = service.buscar("pikachu");

        assertEquals(25, resposta.id());
        assertEquals("pikachu", resposta.name());
        assertEquals(0.4, resposta.heightMeters());
        assertEquals(6.0, resposta.weightKg());
        assertEquals("https://example.com/pikachu.png", resposta.imageUrl());
        assertEquals("electric", resposta.types().getFirst());
        assertEquals(2, resposta.abilities().size());
    }

    @Test
    void aceitaBuscaPeloNumero() {
        when(client.buscar("25")).thenReturn(pokemon());

        assertEquals(25, service.buscar("25").id());

        verify(client).buscar("25");
    }

    @Test
    void normalizaNomeAntesDaConsulta() {
        when(client.buscar("pikachu")).thenReturn(pokemon());

        service.buscar(" PiKaChU ");

        verify(client).buscar("pikachu");
    }

    @Test
    void rejeitaEntradaInvalidaSemConsultarApi() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.buscar("pikachu!")
        );

        verifyNoInteractions(client);
    }

    @Test
    void rejeitaRespostaInvalidaDaApi() {
        when(client.buscar("pikachu")).thenReturn(
            JsonMapper.builder().build().readTree("{}")
        );

        assertThrows(
            UpstreamServiceException.class,
            () -> service.buscar("pikachu")
        );
    }

    @Test
    void preservaErroDePokemonInexistente() {
        when(client.buscar("desconhecido"))
            .thenThrow(new PokemonNotFoundException());

        assertThrows(
            PokemonNotFoundException.class,
            () -> service.buscar("desconhecido")
        );
    }
}