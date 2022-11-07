package com.twitter.follows.exceptions;

public class UserAlreadyFollowingException extends RuntimeException {
    public UserAlreadyFollowingException(String message) {
        super(message);
    }
}
