package com.sync;

public class CredentialInitializationException extends RuntimeException {
    public CredentialInitializationException() {
    }

    public CredentialInitializationException( String message ) {
        super( message );
    }

    public CredentialInitializationException( String message, Throwable cause ) {
        super( message, cause );
    }
}
