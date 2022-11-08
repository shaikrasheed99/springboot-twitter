package com.twitter.follows.controller;

import com.twitter.users.model.IUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowsResponseBody {
    private int count;
    private List<IUser> follows;

    public FollowsResponseBody() {
    }

    public FollowsResponseBody(int count, List<IUser> follows) {
        this.count = count;
        this.follows = follows;
    }

    public int getCount() {
        return count;
    }

    public List<IUser> getFollows() {
        return follows;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFollows(List<IUser> follows) {
        this.follows = follows;
    }

    @Override
    public String toString() {
        return "FollowsResponseBody{" + "count=" + count + ", follows=" + follows + '}';
    }
}
