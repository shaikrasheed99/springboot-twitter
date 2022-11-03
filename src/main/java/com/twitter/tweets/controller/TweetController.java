package com.twitter.tweets.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.helpers.SuccessResponse;
import com.twitter.tweets.repository.Tweet;
import com.twitter.tweets.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/users")
public class TweetController {
    private TweetService tweetService;

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
}
