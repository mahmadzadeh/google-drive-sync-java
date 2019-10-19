package com.sync;

import java.io.IOException;

public class RemoteFileCreationException extends RuntimeException {

    public RemoteFileCreationException() {
    }

    public RemoteFileCreationException( String message ) {
        super( message );
    }

    public RemoteFileCreationException( String message, Throwable cause ) {
        super( message, cause );
    }
}
