package com.twitter.tweets.service;

import com.twitter.tweets.exceptions.AuthorMismatchException;
import com.twitter.tweets.exceptions.InvalidTweetRequestBodyException;
import com.twitter.tweets.exceptions.TweetNotFoundException;
import com.twitter.tweets.model.Tweet;
import com.twitter.tweets.model.TweetRepository;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.model.User;
import com.twitter.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TweetServiceTest {
    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private TweetService tweetService;
    private User user;
    private Tweet tweet;
    private String description;

    @BeforeEach
    void setUp() {
        user = new User("Ironman");
        description = "This is first tweet";
        tweet = new Tweet(description, user);
    }

    @Test
    void shouldBeAbleToCreateTweet() {
        when(userService.getById(user.getId())).thenReturn(user);
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);

        Tweet createdTweet = tweetService.create(description, user.getId());

        assertEquals(user.getId(), createdTweet.getUser().getId());
        assertEquals(description, createdTweet.getDescription());

        verify(userService, times(1)).getById(user.getId());
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenDescriptionIsNull() {
        assertThrows(InvalidTweetRequestBodyException.class, () -> tweetService.create(null, 1));
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenDescriptionIsEmpty() {
        String description = "";

        assertThrows(InvalidTweetRequestBodyException.class, () -> tweetService.create(description, 1));
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenAuthorIdIsNotPresentInUserRepository() {
        when(userService.getById(user.getId())).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> tweetService.create(description, user.getId()));
    }

    @Test
    void shouldBeAbleToGetTweetById() {
        when(tweetRepository.findById(tweet.getId())).thenReturn(Optional.ofNullable(tweet));

        Tweet tweetById = tweetService.getById(tweet.getId());

        assertEquals(tweetById.getId(), tweet.getId());
        assertEquals(tweetById.getDescription(), tweet.getDescription());
        assertEquals(tweetById.getUser(), tweet.getUser());

        verify(tweetRepository, times(1)).findById(tweet.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenTweetIsNotFoundById() {
        when(tweetRepository.findById(tweet.getId())).thenReturn(Optional.empty());

        assertThrows(TweetNotFoundException.class, () -> tweetService.getById(tweet.getId()));
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenAuthorIsNotFound() {
        when(userService.getById(user.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> tweetService.getByAuthorIdAndTweetId(user.getId(), tweet.getId()));
    }

    @Test
    void shouldBeAbleToGetTweetsByUserId() {
        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet);
        tweets.add(tweet);
        tweets.add(tweet);
        when(tweetRepository.findAllByAuthorId(user.getId())).thenReturn(tweets);
        when(userService.getById(user.getId())).thenReturn(user);

        List<Tweet> tweetsByUserId = tweetService.getByAuthorId(user.getId());

        assertEquals(tweetsByUserId.size(), tweets.size());

        verify(tweetRepository, times(1)).findAllByAuthorId(user.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFindingTweetsOfNotExistingUser() {
        when(userService.getById(user.getId())).thenThrow(new UserNotFoundException("User not found!"));

        assertThrows(UserNotFoundException.class, () -> tweetService.getByAuthorId(user.getId()));

        verify(userService, times(1)).getById(user.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenAuthorMismatchWithTweetDetails() {
        User anotherAuthor = new User("Captain");
        when(userService.getById(anotherAuthor.getId() + 1)).thenReturn(anotherAuthor);
        when(tweetRepository.findById(tweet.getId())).thenReturn(Optional.ofNullable(tweet));

        assertThrows(AuthorMismatchException.class, () -> tweetService.getByAuthorIdAndTweetId(1, tweet.getId()));
    }
}
