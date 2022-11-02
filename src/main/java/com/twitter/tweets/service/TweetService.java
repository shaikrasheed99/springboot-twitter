package com.twitter.tweets.service;

import com.twitter.tweets.exceptions.InvalidTweetRequestBodyException;
import com.twitter.tweets.repository.Tweet;
import com.twitter.tweets.repository.TweetRepository;
import com.twitter.users.repository.User;
import com.twitter.users.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class TweetService {
    private final TweetRepository tweetRepository;
    private final UserService userService;

    public TweetService(TweetRepository tweetRepository, UserService userService) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
    }

    public Tweet create(String description, int authorId) {
        ArrayList<Object> errors = new ArrayList<>();
        if (isDescriptionNull(description) || description.isEmpty())
            errors.add(Collections.singletonMap("message", "Description should not be null or empty!"));

        User author = null;
        try {
            author = userService.getUserById(authorId);
        } catch (Exception exception) {
            errors.add(Collections.singletonMap("message", exception.getMessage()));
        }

        if (!errors.isEmpty()) throw new InvalidTweetRequestBodyException(errors);

        Tweet tweet = new Tweet(description, author);
        return tweetRepository.save(tweet);
    }

    private static boolean isDescriptionNull(String description) {
        return description == null;
    }
}
