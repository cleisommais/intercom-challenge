package com.cleison.itercom.challenge.exeptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogicExeption extends RuntimeException {
    public LogicExeption(String message) {
        super(message);
        log.error(message);
    }

    public LogicExeption(String message, Throwable cause) {
        super(message, cause);
        log.error(message);
    }
}
