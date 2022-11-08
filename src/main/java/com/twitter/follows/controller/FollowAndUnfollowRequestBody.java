package com.twitter.follows.controller;

import org.springframework.stereotype.Component;

@Component
public class FollowAndUnfollowRequestBody {
    private int followsId;

    public FollowAndUnfollowRequestBody() {
    }

    public FollowAndUnfollowRequestBody(int followsId) {
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
