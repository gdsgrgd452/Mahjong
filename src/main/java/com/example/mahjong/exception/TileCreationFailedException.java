package com.example.mahjong.exception;

public class TileCreationFailedException extends RuntimeException {

    public TileCreationFailedException(String message) {
        super(message);
    }

    public TileCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
