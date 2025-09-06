package com.jns.app_manager.exceptions;

public class ViolationIntegrityException extends RuntimeException {
    public ViolationIntegrityException(String message) {
        super(message);
    }
}