package com.twitter.follows.controller;

import org.springframework.stereotype.Component;

@Component
public class FollowResponseBody {
    private int followerId;
    private int followsId;

    public FollowResponseBody() {
    }

    public FollowResponseBody(int followerId, int followsId) {
        this.followerId = followerId;
        this.followsId = followsId;
    }

    public int getFollowerId() {
        return followerId;
    }

    public int getFollowsId() {
        return followsId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public void setFollowsId(int followsId) {
        this.followsId = followsId;
    }

    @Override
    public String toString() {
        return "FollowResponseBody{" + "followerId=" + followerId + ", followsId=" + followsId + '}';
    }
}
