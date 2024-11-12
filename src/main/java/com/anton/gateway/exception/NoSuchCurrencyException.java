package com.anton.gateway.exception;

public class NoSuchCurrencyException extends RuntimeException{

    public NoSuchCurrencyException(String currency) {
        super("Currency" + currency + "not supported");
    }
}
