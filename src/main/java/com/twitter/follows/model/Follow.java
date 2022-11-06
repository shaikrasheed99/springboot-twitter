package com.twitter.follows.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "follows")
public class Follow {
    @EmbeddedId
    private FollowsCompositePrimaryKey followsCompositePrimaryKey;

    public Follow() {
    }

    public Follow(FollowsCompositePrimaryKey followsCompositePrimaryKey) {
        this.followsCompositePrimaryKey = followsCompositePrimaryKey;
    }

    public FollowsCompositePrimaryKey getFollowsCompositePrimaryKey() {
        return followsCompositePrimaryKey;
    }

    @Override
    public String toString() {
        return "Follow{" + "followsCompositePrimaryKey=" + followsCompositePrimaryKey + '}';
    }
}
