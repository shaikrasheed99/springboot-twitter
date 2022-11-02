package com.twitter.tweets.repository;

import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<Tweet, Integer> {
}
