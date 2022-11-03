package com.twitter.tweets.model;

import com.twitter.users.model.User;

import javax.persistence.*;

@Entity
@Table(name = "tweets")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String description;
    @ManyToOne
    @JoinColumn(name = "authorId", referencedColumnName = "id")
    private User user;

    public Tweet() {
    }

    public Tweet(String description, User user) {
        this.description = description;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Tweet{" + "id=" + id + ", description='" + description + '\'' + ", user=" + user + '}';
    }
}
