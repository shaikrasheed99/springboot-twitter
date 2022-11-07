package com.twitter.follows.controller;

import org.springframework.stereotype.Component;

@Component
public class FollowRequestBody {
    private int followsId;

    public FollowRequestBody() {
    }

    public FollowRequestBody(int followsId) {
        this.followsId = followsId;
    }

    public int getFollowsId() {
        return followsId;
    }

    @Override
    public String toString() {
        return "FollowRequestBody{" + "followsId=" + followsId + '}';
    }
}
