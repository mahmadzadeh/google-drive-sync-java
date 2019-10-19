package com.sync;

import java.io.IOException;

public class ClientSecretReadException extends RuntimeException {
    public ClientSecretReadException() {
    }

    public ClientSecretReadException( String message ) {
        super( message );
    }

    public ClientSecretReadException( String message, Throwable cause ) {
        super( message, cause );
    }
}
