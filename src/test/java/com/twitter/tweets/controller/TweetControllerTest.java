package com.twitter.tweets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.tweets.exceptions.InvalidTweetRequestBodyException;
import com.twitter.tweets.repository.Tweet;
import com.twitter.tweets.service.TweetService;
import com.twitter.users.repository.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
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

        ResultActions result = mockMvc.perform(post("/users/{id}/tweets", user.getId()).contentType(MediaType.APPLICATION_JSON).content(tweetRequestBodyJson));

        result.andExpect(status().isCreated()).andExpect(jsonPath("$.data.tweet_id").value(tweet.getId())).andDo(print());

        verify(tweetService, times(1)).create(description, user.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenDescriptionIsEmpty() throws Exception {
        when(tweetService.create("", user.getId())).thenThrow(new InvalidTweetRequestBodyException(Collections.singletonList("Description should not be empty!")));
        String tweetRequestBodyJson = new ObjectMapper().writeValueAsString(new TweetRequestBody(""));

        ResultActions result = mockMvc.perform(post("/users/{id}/tweets", user.getId()).contentType(MediaType.APPLICATION_JSON).content(tweetRequestBodyJson));

        result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("Description should not be empty!")).andDo(print());

        verify(tweetService, times(1)).create("", user.getId());
    }
}
