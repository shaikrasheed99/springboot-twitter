package com.twitter.tweets.service;

import com.twitter.tweets.exceptions.InvalidTweetRequestBodyException;
import com.twitter.tweets.repository.Tweet;
import com.twitter.tweets.repository.TweetRepository;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.repository.User;
import com.twitter.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);

        Tweet createdTweet = tweetService.create(description, user.getId());

        assertEquals(user.getId(), createdTweet.getUser().getId());
        assertEquals(description, createdTweet.getDescription());

        verify(userService, times(1)).getUserById(user.getId());
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
        when(userService.getUserById(user.getId())).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(InvalidTweetRequestBodyException.class, () -> tweetService.create(description, user.getId()));
    }
}
