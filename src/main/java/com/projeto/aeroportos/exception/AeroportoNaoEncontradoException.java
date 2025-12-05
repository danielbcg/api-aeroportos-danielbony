package com.projeto.aeroportos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AeroportoNaoEncontradoException extends RuntimeException {
    
    public AeroportoNaoEncontradoException(String codigoIata) {
        super("Aeroporto com código IATA '" + codigoIata + "' não encontrado.");
    }
    
    public AeroportoNaoEncontradoException(Long id) {
        super("Aeroporto com ID '" + id + "' não encontrado.");
    }
}