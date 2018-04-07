package com.acmeexchange.exceptions;

public class InvalidOrderStringException extends Exception {

    public InvalidOrderStringException(String error) {
        super(error);
    }
}
