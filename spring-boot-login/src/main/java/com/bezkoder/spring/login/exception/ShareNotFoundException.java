package com.bezkoder.spring.login.exception;

public class ShareNotFoundException extends RuntimeException {
    public ShareNotFoundException() {
    }

    public ShareNotFoundException(String message) {
        super(message);
    }
}
