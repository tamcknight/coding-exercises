package com.acmeexchange.exceptions;

public class InvalidOrderIdException extends Exception {

    public InvalidOrderIdException(String error) {
        super(error);
    }
}
