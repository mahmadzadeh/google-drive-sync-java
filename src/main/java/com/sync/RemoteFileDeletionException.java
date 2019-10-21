package com.sync;

public class RemoteFileDeletionException extends RuntimeException {
    public RemoteFileDeletionException(String message) {
        super(message);
    }

    public RemoteFileDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
