package com.twitter.tweets.service;

import com.twitter.tweets.exceptions.AuthorMismatchException;
import com.twitter.tweets.exceptions.InvalidTweetRequestBodyException;
import com.twitter.tweets.exceptions.TweetNotFoundException;
import com.twitter.tweets.model.Tweet;
import com.twitter.tweets.model.TweetRepository;
import com.twitter.users.model.User;
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

        if (!errors.isEmpty()) throw new InvalidTweetRequestBodyException(errors);

        User author = userService.getUserById(authorId);

        Tweet tweet = new Tweet(description, author);
        return tweetRepository.save(tweet);
    }

    public Tweet getById(int id) {
        Optional<Tweet> tweet = tweetRepository.findById(id);

        if (tweet.isEmpty()) throw new TweetNotFoundException("Tweet is not found with that id!");

        return tweet.get();
    }

    public Tweet getByAuthorIdAndTweetId(int authorId, int tweetId) {
        User author = userService.getUserById(authorId);
        Tweet tweet = getById(tweetId);

        if (tweet.getUser().getId() != authorId)
            throw new AuthorMismatchException("Author cannot see another author's complete tweet details!");

        return tweet;
    }

    public List<Tweet> getByAuthorId(int id) {
        User author = userService.getUserById(id);

        return tweetRepository.findAllByAuthorId(author.getId());
    }

    private static boolean isDescriptionNull(String description) {
        return description == null;
    }
}
