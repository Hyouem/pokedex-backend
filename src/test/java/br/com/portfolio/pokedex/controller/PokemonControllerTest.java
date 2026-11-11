package br.com.portfolio.pokedex.controller;

import br.com.portfolio.pokedex.dto.PokemonResponse;
import br.com.portfolio.pokedex.exception.ApiExceptionHandler;
import br.com.portfolio.pokedex.exception.PokemonNotFoundException;
import br.com.portfolio.pokedex.exception.UpstreamServiceException;
import br.com.portfolio.pokedex.service.PokemonService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PokemonControllerTest {

    private PokemonService service;
    private MockMvc mvc;

    @BeforeEach
    void configurar() {
        service = mock(PokemonService.class);

        mvc = MockMvcBuilders
            .standaloneSetup(new PokemonController(service))
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
    }

    @Test
    void retornaPokemonComStatus200() throws Exception {
        when(service.buscar("pikachu")).thenReturn(
            new PokemonResponse(
                25,
                "pikachu",
                "https://example.com/25.png",
                List.of("electric"),
                0.4,
                6.0,
                List.of("static")
            )
        );

        mvc.perform(get("/api/pokemon/pikachu"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(25))
            .andExpect(jsonPath("$.name").value("pikachu"))
            .andExpect(jsonPath("$.types[0]").value("electric"));
    }

    @Test
    void retorna400ParaBuscaInvalida() throws Exception {
        when(service.buscar("pikachu!"))
            .thenThrow(new IllegalArgumentException());

        mvc.perform(get("/api/pokemon/pikachu!"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("INVALID_QUERY"));
    }

    @Test
    void retorna404ParaPokemonInexistente() throws Exception {
        when(service.buscar("desconhecido"))
            .thenThrow(new PokemonNotFoundException());

        mvc.perform(get("/api/pokemon/desconhecido"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("POKEMON_NOT_FOUND"));
    }

    @Test
    void retorna502ParaFalhaDaPokeApi() throws Exception {
        when(service.buscar("pikachu"))
            .thenThrow(new UpstreamServiceException());

        mvc.perform(get("/api/pokemon/pikachu"))
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.code").value("POKEAPI_ERROR"));
    }
}