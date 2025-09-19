package com.cliapp.exceptions;

public class NoSavedGameException extends Exception {

    public NoSavedGameException(String message) {
        super(message);

    }
public NoSavedGameException(String message, Throwable cause) {
    super(message, cause);
}
}
