package com.anton.gateway.exception;

public class DuplicateRequestException extends RuntimeException{
    public DuplicateRequestException(String requestId) {
        super("Request with ID " + requestId + " already exists.");
    }
}
