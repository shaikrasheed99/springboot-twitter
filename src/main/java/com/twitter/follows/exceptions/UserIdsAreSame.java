package com.twitter.follows.exceptions;

public class UserIdsAreSame extends RuntimeException {
    public UserIdsAreSame(String message) {
        super(message);
    }
}
