package com.dsa.assistant.exception;

public class ProblemNotFoundException extends RuntimeException {
    public ProblemNotFoundException(String message) {
        super(message);
    }
}
