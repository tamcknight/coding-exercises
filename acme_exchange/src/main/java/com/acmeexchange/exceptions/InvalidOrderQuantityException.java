package com.acmeexchange.exceptions;

public class InvalidOrderQuantityException extends Exception {

    public InvalidOrderQuantityException(String error) {
        super(error);
    }
}
