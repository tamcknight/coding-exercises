package com.acmeexchange.exceptions;

public class InvalidOrderSideException extends Exception {

    public InvalidOrderSideException(String error) {
        super(error);
    }
}
