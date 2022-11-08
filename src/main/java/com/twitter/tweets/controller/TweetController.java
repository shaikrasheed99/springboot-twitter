package com.twitter.tweets.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.helpers.SuccessResponse;
import com.twitter.tweets.model.Tweet;
import com.twitter.tweets.service.TweetService;
import com.twitter.users.model.User;
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

    @PostMapping("/{userId}/tweets")
    public ResponseEntity<?> create(@PathVariable int userId, @RequestBody TweetRequestBody tweetRequestBody) throws JsonProcessingException {
        Tweet tweet = tweetService.create(tweetRequestBody.getDescription(), userId);
        successResponse.setData(Collections.singletonMap("tweet_id", tweet.getId()));
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    @GetMapping("/{userId}/tweets")
    public ResponseEntity<?> getByUserId(@PathVariable int userId) throws JsonProcessingException {
        List<Tweet> tweetsByUserId = tweetService.getByAuthorId(userId);

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

    @GetMapping("/{userId}/tweets/{tweetId}")
    public ResponseEntity<?> getById(@PathVariable int userId, @PathVariable int tweetId) throws JsonProcessingException {
        Tweet tweet = tweetService.getByAuthorIdAndTweetId(userId, tweetId);

        User author = tweet.getUser();
        TweetResponseBody tweetResponseBody = new TweetResponseBody(tweet.getId(), tweet.getDescription(), author.getId(), author.getName());

        successResponse.setData(tweetResponseBody);
        String responseJson = successResponse.convertToJson();

        return ResponseEntity.status(HttpStatus.OK).body(responseJson);
    }
}
