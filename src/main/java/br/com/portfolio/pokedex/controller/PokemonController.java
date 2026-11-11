package br.com.portfolio.pokedex.controller;

import br.com.portfolio.pokedex.dto.PokemonResponse;
import br.com.portfolio.pokedex.service.PokemonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    private final PokemonService service;

    public PokemonController(PokemonService service) {
        this.service = service;
    }

    @GetMapping("/{nameOrId}")
    public PokemonResponse buscar(@PathVariable String nameOrId) {
        return service.buscar(nameOrId);
    }
}