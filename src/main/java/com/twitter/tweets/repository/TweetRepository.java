package com.twitter.tweets.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TweetRepository extends CrudRepository<Tweet, Integer> {
    @Query(value = "SELECT * FROM tweets WHERE author_id = :id", nativeQuery = true)
    List<Tweet> findAllByAuthorId(int id);
}
