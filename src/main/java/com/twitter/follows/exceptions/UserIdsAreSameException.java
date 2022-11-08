package com.twitter.follows.exceptions;

public class UserIdsAreSameException extends RuntimeException {
    public UserIdsAreSameException(String message) {
        super(message);
    }
}
