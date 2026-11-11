package br.com.portfolio.pokedex.exception;

public class PokemonNotFoundException extends RuntimeException {

    public PokemonNotFoundException() {
        super("Pokemon nao encontrado.");
    }
}