package com.twitter.follows.controller;

import com.twitter.users.model.IUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowersResponseBody {
    private int count;
    private List<IUser> followers;

    public FollowersResponseBody() {
    }

    public FollowersResponseBody(int count, List<IUser> followers) {
        this.count = count;
        this.followers = followers;
    }

    public int getCount() {
        return count;
    }

    public List<IUser> getFollowers() {
        return followers;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFollowers(List<IUser> followers) {
        this.followers = followers;
    }

    @Override
    public String toString() {
        return "FollowersResponseBody{" + "count=" + count + ", followers=" + followers + '}';
    }
}
