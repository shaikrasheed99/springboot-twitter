package com.twitter.users.exceptions;

public class UserNameNullException extends RuntimeException {
    public UserNameNullException(String message) {
        super(message);
    }
}
