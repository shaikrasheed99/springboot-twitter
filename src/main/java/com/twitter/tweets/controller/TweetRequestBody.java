package com.twitter.tweets.controller;

public class TweetRequestBody {
    private String description;

    public TweetRequestBody() {
    }

    public TweetRequestBody(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TweetRequestBody{" + "description='" + description + '\'' + '}';
    }
}
