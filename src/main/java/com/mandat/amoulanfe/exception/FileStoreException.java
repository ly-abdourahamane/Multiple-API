package com.mandat.amoulanfe.exception;

public class FileStoreException extends RuntimeException {
    public FileStoreException(String message) {
        super(message);
    }

    public FileStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
