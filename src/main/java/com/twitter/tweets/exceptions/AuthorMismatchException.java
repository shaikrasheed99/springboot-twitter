package com.twitter.tweets.exceptions;

public class AuthorMismatchException extends RuntimeException {
    public AuthorMismatchException(String message) {
        super(message);
    }
}
