package com.twitter.tweets.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TweetRepository extends CrudRepository<Tweet, Integer> {
    @Query(value = "SELECT * FROM tweets WHERE author_id = :id", nativeQuery = true)
    List<Tweet> findAllByAuthorId(@Param("id") int id);
}
