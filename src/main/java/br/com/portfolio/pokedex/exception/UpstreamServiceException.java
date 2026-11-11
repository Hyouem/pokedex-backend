package br.com.portfolio.pokedex.exception;

public class UpstreamServiceException extends RuntimeException {

    public UpstreamServiceException() {
        super("Falha na integracao com a PokeAPI.");
    }

    public UpstreamServiceException(Throwable cause) {
        super("Falha na integracao com a PokeAPI.", cause);
    }
}