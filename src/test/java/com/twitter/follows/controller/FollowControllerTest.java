package com.twitter.follows.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.follows.exceptions.UserAlreadyFollowingException;
import com.twitter.follows.exceptions.UserNotFollowingException;
import com.twitter.follows.model.Follow;
import com.twitter.follows.model.FollowsCompositePrimaryKey;
import com.twitter.follows.service.FollowService;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.model.IUser;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FollowControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FollowService followService;
    private User follower;
    private User follows;
    private Follow follow;
    private FollowRequestBody followRequestBody;

    @BeforeEach
    void setUp() {
        follower = new User(1, "Ironman");
        follows = new User(2, "Thor");
        FollowsCompositePrimaryKey primaryKey = new FollowsCompositePrimaryKey(follower, follows);
        follow = new Follow(primaryKey);
        followRequestBody = new FollowRequestBody(follows.getId());
    }

    @Test
    void shouldBeAbleToFollowAUser() throws Exception {
        when(followService.follow(follower.getId(), follows.getId())).thenReturn(follow);
        String requestJson = new ObjectMapper().writeValueAsString(followRequestBody);

        ResultActions result = mockMvc.perform(post("/users/{userId}/follow", follower.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson));

        result.andExpect(status().isOk()).andExpect(jsonPath("$.data.followerId").value(follower.getId())).andExpect(jsonPath("$.data.followsId").value(follows.getId())).andDo(print());

        verify(followService, times(1)).follow(follower.getId(), follows.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenUserIsAlreadyFollowingAnotherUser() throws Exception {
        when(followService.follow(follower.getId(), follows.getId())).thenThrow(new UserAlreadyFollowingException("User already following!"));
        String requestJson = new ObjectMapper().writeValueAsString(followRequestBody);

        ResultActions result = mockMvc.perform(post("/users/{userId}/follow", follower.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson));

        result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error.message").value("User already following!")).andDo(print());

        verify(followService, times(1)).follow(follower.getId(), follows.getId());
    }

    @Test
    void shouldBeAbleToReturnFollowersOfAUser() throws Exception {
        ArrayList<IUser> followers = new ArrayList<>();
        followers.add(follows);
        when(followService.followers(follower.getId())).thenReturn(followers);

        ResultActions result = mockMvc.perform(get("/users/{userId}/followers", follower.getId()));

        result.andExpect(status().isOk()).andExpect(jsonPath("$.data.count").value(followers.size())).andExpect(jsonPath("$.data.followers[0].name").value(followers.get(0).getName())).andDo(print());

        verify(followService, times(1)).followers(follower.getId());
    }

    @Test
    void shouldBeAbleToGiveNotFoundErrorResponseWhenFollowerUserIdDoesNotExists() throws Exception {
        when(followService.followers(follower.getId())).thenThrow(new UserNotFoundException("User not found!"));

        ResultActions result = mockMvc.perform(get("/users/{userId}/followers", follower.getId()));

        result.andExpect(status().isNotFound()).andExpect(jsonPath("$.error.message").value("User not found!")).andDo(print());

        verify(followService, times(1)).followers(follower.getId());
    }

    @Test
    void shouldBeAbleToReturnFollowsOfAUser() throws Exception {
        ArrayList<IUser> followings = new ArrayList<>();
        followings.add(follower);
        when(followService.follows(follows.getId())).thenReturn(followings);

        ResultActions result = mockMvc.perform(get("/users/{userId}/follows", follows.getId()));

        result.andExpect(status().isOk()).andExpect(jsonPath("$.data.count").value(followings.size())).andExpect(jsonPath("$.data.follows[0].name").value(followings.get(0).getName())).andDo(print());

        verify(followService, times(1)).follows(follows.getId());
    }

    @Test
    void shouldBeAbleToGiveNotFoundErrorResponseWhenFollowsUserIdDoesNotExists() throws Exception {
        when(followService.follows(follows.getId())).thenThrow(new UserNotFoundException("User not found!"));

        ResultActions result = mockMvc.perform(get("/users/{userId}/follows", follows.getId()));

        result.andExpect(status().isNotFound()).andExpect(jsonPath("$.error.message").value("User not found!")).andDo(print());

        verify(followService, times(1)).follows(follows.getId());
    }

    @Test
    void shouldBeAbleToUnfollowAUser() throws Exception {
        String requestJson = new ObjectMapper().writeValueAsString(followRequestBody);

        ResultActions result = mockMvc.perform(post("/users/{userId}/unfollow", follower.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson));

        result.andExpect(status().isOk()).andExpect(jsonPath("$.data.message").value("User Id 1 unfollowed User Id 2!")).andDo(print());

        verify(followService, times(1)).unfollow(follower.getId(), follows.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenUserIsNotFollowingAnotherUser() throws Exception {
        when(followService.unfollow(follower.getId(), follows.getId())).thenThrow(new UserNotFollowingException("User not following"));
        String requestJson = new ObjectMapper().writeValueAsString(followRequestBody);

        ResultActions result = mockMvc.perform(post("/users/{userId}/unfollow", follower.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson));

        result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error.message").value("User not following")).andDo(print());

        verify(followService, times(1)).unfollow(follower.getId(), follows.getId());
    }
}