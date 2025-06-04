package com.example.methods.service.exceptions;

public class NoRootFoundException extends RuntimeException {
    public NoRootFoundException(String message) {
        super(message);
    }
}