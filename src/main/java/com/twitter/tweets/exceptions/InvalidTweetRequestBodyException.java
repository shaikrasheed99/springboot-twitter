package com.twitter.tweets.exceptions;

import java.util.List;

public class InvalidTweetRequestBodyException extends RuntimeException {
    private List<Object> errors;

    public InvalidTweetRequestBodyException(List<Object> errors) {
        super();
        this.errors = errors;
    }

    public List<Object> getErrors() {
        return errors;
    }
}
