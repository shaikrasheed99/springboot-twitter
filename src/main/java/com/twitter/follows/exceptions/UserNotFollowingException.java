package com.twitter.follows.exceptions;

public class UserNotFollowingException extends RuntimeException {
    public UserNotFollowingException(String message) {
        super(message);
    }
}
