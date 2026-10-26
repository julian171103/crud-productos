package com.store.productcrud.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("No existe un producto con id " + id);
    }
}