package com.twitter.follows.model;

import com.twitter.users.model.User;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class FollowsCompositePrimaryKey implements Serializable {
    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "follows_id", referencedColumnName = "id")
    private User follows;

    public FollowsCompositePrimaryKey() {
    }

    public FollowsCompositePrimaryKey(User follower, User follows) {
        this.follower = follower;
        this.follows = follows;
    }

    public User getFollower() {
        return follower;
    }

    public User getFollows() {
        return follows;
    }

    @Override
    public String toString() {
        return "FollowsCompositePrimaryKey{" + "follower=" + follower + ", follows=" + follows + '}';
    }
}
