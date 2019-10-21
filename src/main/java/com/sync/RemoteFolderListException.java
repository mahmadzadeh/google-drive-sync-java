package com.sync;

import java.io.IOException;

public class RemoteFolderListException extends RuntimeException {

    public RemoteFolderListException(String message) {
        super(message);
    }

    public RemoteFolderListException(String message, Throwable cause) {
        super(message, cause);
    }
}
