package com.twitter.tweets.exceptions;

public class TweetNotFoundException extends RuntimeException {
    public TweetNotFoundException(String message) {
        super(message);
    }
}
