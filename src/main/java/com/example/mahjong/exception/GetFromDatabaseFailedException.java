package com.example.mahjong.exception;

public class GetFromDatabaseFailedException extends RuntimeException {

    public GetFromDatabaseFailedException(String message) {
        super(message);
    }

    public GetFromDatabaseFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}