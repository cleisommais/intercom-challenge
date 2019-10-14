package com.cleison.itercom.challenge.exeptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
        log.error(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
