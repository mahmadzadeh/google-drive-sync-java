package com.sync.util;

public class InvalidConfigurationFileException extends RuntimeException {

    public InvalidConfigurationFileException(String message) {
        super(message);
    }

    public InvalidConfigurationFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
