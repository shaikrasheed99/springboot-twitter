package com.twitter.tweets.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.helpers.SuccessResponse;
import com.twitter.tweets.repository.Tweet;
import com.twitter.tweets.service.TweetService;
import com.twitter.users.repository.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class TweetController {
    private final TweetService tweetService;

    @Autowired
    private SuccessResponse successResponse;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping("/{id}/tweets")
    public ResponseEntity<?> create(@PathVariable int id, @RequestBody TweetRequestBody tweetRequestBody) throws JsonProcessingException {
        Tweet tweet = tweetService.create(tweetRequestBody.getDescription(), id);
        successResponse.setData(Collections.singletonMap("tweet_id", tweet.getId()));
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    @GetMapping("/{id}/tweets")
    public ResponseEntity<?> getByUserId(@PathVariable int id) throws JsonProcessingException {
        List<Tweet> tweetsByUserId = tweetService.getByAuthorId(id);

        ArrayList<TweetResponseBody> tweets = new ArrayList<>();
        tweetsByUserId.forEach(tweet -> {
            User author = tweet.getUser();
            TweetResponseBody tweetResponseBody = new TweetResponseBody(tweet.getId(), tweet.getDescription(), author.getId(), author.getName());
            tweets.add(tweetResponseBody);
        });

        successResponse.setData(tweets);
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.OK).body(responseJson);
    }

    @GetMapping("/{id}/tweets/{tweetId}")
    public ResponseEntity<?> getById(@PathVariable int id, @PathVariable int tweetId) throws JsonProcessingException {
        Tweet tweet = tweetService.getByAuthorIdAndTweetId(id, tweetId);

        User author = tweet.getUser();
        TweetResponseBody tweetResponseBody = new TweetResponseBody(tweet.getId(), tweet.getDescription(), author.getId(), author.getName());

        successResponse.setData(tweetResponseBody);
        String responseJson = successResponse.convertToJson();

        return ResponseEntity.status(HttpStatus.OK).body(responseJson);
    }
}
