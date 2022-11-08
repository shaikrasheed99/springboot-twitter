package com.twitter.tweets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.tweets.exceptions.AuthorMismatchException;
import com.twitter.tweets.exceptions.InvalidTweetRequestBodyException;
import com.twitter.tweets.exceptions.TweetNotFoundException;
import com.twitter.tweets.model.Tweet;
import com.twitter.tweets.service.TweetService;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TweetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TweetService tweetService;
    private String description;
    private User user;
    private Tweet tweet;
    private TweetRequestBody tweetRequestBody;

    @BeforeEach
    void setUp() {
        user = new User("Ironman");
        description = "This is first tweet!";
        tweetRequestBody = new TweetRequestBody(description);
        tweet = new Tweet(description, user);
    }

    @Test
    void shouldBeAbleToCreateTweet() throws Exception {
        when(tweetService.create(description, user.getId())).thenReturn(tweet);
        String tweetRequestBodyJson = new ObjectMapper().writeValueAsString(tweetRequestBody);

        ResultActions result = mockMvc.perform(post("/users/{id}/tweets", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(tweetRequestBodyJson));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tweet_id").value(tweet.getId()))
                .andDo(print());

        verify(tweetService, times(1)).create(description, user.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenDescriptionIsEmpty() throws Exception {
        when(tweetService.create("", user.getId())).thenThrow(new InvalidTweetRequestBodyException(Collections.singletonList("Description should not be empty!")));
        String tweetRequestBodyJson = new ObjectMapper().writeValueAsString(new TweetRequestBody(""));

        ResultActions result = mockMvc.perform(post("/users/{id}/tweets", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(tweetRequestBodyJson));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Description should not be empty!"))
                .andDo(print());

        verify(tweetService, times(1)).create("", user.getId());
    }

    @Test
    void shouldBeAbleToGetTweetsByUserId() throws Exception {
        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet);
        tweets.add(tweet);
        tweets.add(tweet);
        when(tweetService.getByAuthorId(user.getId())).thenReturn(tweets);

        ResultActions result = mockMvc.perform(get("/users/{id}/tweets", user.getId()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].description").value(tweets.get(0).getDescription()))
                .andDo(print());

        verify(tweetService, times(1)).getByAuthorId(user.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenAuthorIdIsNotPresent() throws Exception {
        when(tweetService.getByAuthorId(user.getId())).thenThrow(new UserNotFoundException("User not found!"));

        ResultActions result = mockMvc.perform(get("/users/{id}/tweets", user.getId()));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("User not found!"))
                .andDo(print());

        verify(tweetService, times(1)).getByAuthorId(user.getId());
    }

    @Test
    void shouldBeAbleToGetTweetsByUserIdAndTweetId() throws Exception {
        when(tweetService.getByAuthorIdAndTweetId(user.getId(), tweet.getId())).thenReturn(tweet);

        ResultActions result = mockMvc.perform(get("/users/{id}/tweets/{tweetId}", user.getId(), tweet.getId()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(tweet.getId()))
                .andExpect(jsonPath("$.data.description").value(tweet.getDescription()))
                .andDo(print());

        verify(tweetService, times(1)).getByAuthorIdAndTweetId(user.getId(), tweet.getId());
    }

    @Test
    void shouldBeAbleToGiveTweetNotFoundErrorResponseMessage() throws Exception {
        when(tweetService.getByAuthorIdAndTweetId(user.getId(), tweet.getId())).thenThrow(new TweetNotFoundException("Tweet not found!"));

        ResultActions result = mockMvc.perform(get("/users/{id}/tweets/{tweetId}", user.getId(), tweet.getId()));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Tweet not found!"))
                .andDo(print());

        verify(tweetService, times(1)).getByAuthorIdAndTweetId(user.getId(), tweet.getId());
    }

    @Test
    void shouldBeAbleToGiveForbiddenErrorResponseMessage() throws Exception {
        when(tweetService.getByAuthorIdAndTweetId(user.getId(), tweet.getId())).thenThrow(new AuthorMismatchException("Author mismatch!"));

        ResultActions result = mockMvc.perform(get("/users/{id}/tweets/{tweetId}", user.getId(), tweet.getId()));

        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.message").value("Author mismatch!"))
                .andDo(print());

        verify(tweetService, times(1)).getByAuthorIdAndTweetId(user.getId(), tweet.getId());
    }
}
