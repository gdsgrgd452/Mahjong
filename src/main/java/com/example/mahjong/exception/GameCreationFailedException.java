package com.example.mahjong.exception;

public class GameCreationFailedException extends RuntimeException {

    public GameCreationFailedException(String message) {
        super(message);
    }

    public GameCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}