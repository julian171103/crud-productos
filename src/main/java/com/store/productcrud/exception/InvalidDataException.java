package com.store.productcrud.exception;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String mensaje) {
        super(mensaje);
    }
}