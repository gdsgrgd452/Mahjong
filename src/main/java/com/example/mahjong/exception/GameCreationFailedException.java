package com.example.mahjong.exception;

// We use RuntimeException so we don't have to add "throws" to every method signature
public class GameCreationFailedException extends RuntimeException {

    public GameCreationFailedException(String message) {
        super(message);
    }

    public GameCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}