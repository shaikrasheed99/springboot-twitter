package com.twitter.tweets.controller;

public class TweetResponseBody {
    private int id;
    private String description;
    private int authorId;
    private String authorName;

    public TweetResponseBody() {
    }

    public TweetResponseBody(int id, String description, int authorId, String authorName) {
        this.id = id;
        this.description = description;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public String toString() {
        return "TweetResponseBody{" + "id=" + id + ", description='" + description + '\'' + ", authorId=" + authorId + ", authorName='" + authorName + '\'' + '}';
    }
}
