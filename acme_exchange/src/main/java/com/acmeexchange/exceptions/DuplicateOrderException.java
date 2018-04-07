package com.acmeexchange.exceptions;

public class DuplicateOrderException extends Exception {

    public DuplicateOrderException(String error){
        super(error);
    }
}
