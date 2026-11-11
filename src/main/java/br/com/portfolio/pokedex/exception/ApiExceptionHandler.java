package br.com.portfolio.pokedex.exception;

import br.com.portfolio.pokedex.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> entradaInvalida() {
        return ResponseEntity.badRequest()
            .body(new ApiError(
                "INVALID_QUERY",
                "Informe um nome ou numero de Pokemon valido."
            ));
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    public ResponseEntity<ApiError> naoEncontrado() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError(
                "POKEMON_NOT_FOUND",
                "Pokemon nao encontrado."
            ));
    }

    @ExceptionHandler(UpstreamServiceException.class)
    public ResponseEntity<ApiError> apiIndisponivel() {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(new ApiError(
                "POKEAPI_ERROR",
                "Nao foi possivel consultar a PokeAPI."
            ));
    }
}