package com.acmeexchange.exceptions;

public class InvalidOrderPriceException extends Exception {

    public InvalidOrderPriceException(String error) {
        super(error);
    }
}
