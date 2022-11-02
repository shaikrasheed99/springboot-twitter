package com.twitter.tweets.service;

import com.twitter.tweets.exceptions.InvalidTweetRequestBodyException;
import com.twitter.tweets.exceptions.TweetNotFoundException;
import com.twitter.tweets.repository.Tweet;
import com.twitter.tweets.repository.TweetRepository;
import com.twitter.users.repository.User;
import com.twitter.users.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public Tweet getById(int id) {
        Optional<Tweet> tweet = tweetRepository.findById(id);

        if (tweet.isEmpty()) throw new TweetNotFoundException("Tweet is not found with that id!");

        return tweet.get();
    }

    private static boolean isDescriptionNull(String description) {
        return description == null;
    }

    public List<Tweet> getByUserId(int id) {
        return tweetRepository.findAllByUserId(id);
    }
}
