package br.com.portfolio.pokedex.dto;

import java.util.List;

public record PokemonResponse(
    int id,
    String name,
    String imageUrl,
    List<String> types,
    double heightMeters,
    double weightKg,
    List<String> abilities
) {
}