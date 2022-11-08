package com.twitter.follows.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.follows.exceptions.UserAlreadyFollowingException;
import com.twitter.follows.exceptions.UserIdsAreSameException;
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
import java.util.List;

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
    private User ironman;
    private User thor;
    private User thanos;
    private List<IUser> users;
    private FollowAndUnfollowRequestBody followAndUnfollowRequestBodyContainsThor;
    private Follow ironmanFollowsThor;

    @BeforeEach
    void setUp() {
        ironman = new User(1, "Ironman");
        thor = new User(2, "Thor");
        thanos = new User(3, "Thanos");
        users = new ArrayList<>();
        users.add(ironman);
        users.add(thor);
        users.add(thanos);
        FollowsCompositePrimaryKey ironmanThorKey = new FollowsCompositePrimaryKey(ironman, thor);
        ironmanFollowsThor = new Follow(ironmanThorKey);
        followAndUnfollowRequestBodyContainsThor = new FollowAndUnfollowRequestBody(thor.getId());
    }

    @Test
    void shouldBeAbleToFollowAUser() throws Exception {
        when(followService.follow(ironman.getId(), thor.getId())).thenReturn(ironmanFollowsThor);
        String requestJson = new ObjectMapper().writeValueAsString(followAndUnfollowRequestBodyContainsThor);

        ResultActions result = mockMvc.perform(post("/users/{userId}/follow", ironman.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.followerId").value(ironman.getId()))
                .andExpect(jsonPath("$.data.followsId").value(thor.getId()))
                .andDo(print());

        verify(followService, times(1)).follow(ironman.getId(), thor.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenBothFollowerIdAndFollowsIdAreSameToFollow() throws Exception {
        when(followService.follow(thor.getId(), thor.getId())).thenThrow(new UserIdsAreSameException("Both ids are same!"));
        String requestJson = new ObjectMapper().writeValueAsString(followAndUnfollowRequestBodyContainsThor);

        ResultActions result = mockMvc.perform(post("/users/{userId}/follow", thor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Both ids are same!"))
                .andDo(print());

        verify(followService, times(1)).follow(thor.getId(), thor.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenUserIsAlreadyFollowingAnotherUser() throws Exception {
        when(followService.follow(ironman.getId(), thor.getId())).thenThrow(new UserAlreadyFollowingException("User already following!"));
        String requestJson = new ObjectMapper().writeValueAsString(followAndUnfollowRequestBodyContainsThor);

        ResultActions result = mockMvc.perform(post("/users/{userId}/follow", ironman.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("User already following!"))
                .andDo(print());

        verify(followService, times(1)).follow(ironman.getId(), thor.getId());
    }

    @Test
    void shouldBeAbleToReturnFollowersOfAUser() throws Exception {
        when(followService.followers(ironman.getId())).thenReturn(users);

        ResultActions result = mockMvc.perform(get("/users/{userId}/followers", ironman.getId()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(users.size()))
                .andExpect(jsonPath("$.data.followers[0].id").value(users.get(0).getId()))
                .andExpect(jsonPath("$.data.followers[1].name").value(users.get(1).getName()))
                .andDo(print());

        verify(followService, times(1)).followers(ironman.getId());
    }

    @Test
    void shouldBeAbleToGiveNotFoundErrorResponseWhenFollowerUserIdDoesNotExists() throws Exception {
        when(followService.followers(ironman.getId())).thenThrow(new UserNotFoundException("User not found!"));

        ResultActions result = mockMvc.perform(get("/users/{userId}/followers", ironman.getId()));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("User not found!"))
                .andDo(print());

        verify(followService, times(1)).followers(ironman.getId());
    }

    @Test
    void shouldBeAbleToReturnFollowsOfAUser() throws Exception {
        when(followService.follows(thor.getId())).thenReturn(users);

        ResultActions result = mockMvc.perform(get("/users/{userId}/follows", thor.getId()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(users.size()))
                .andExpect(jsonPath("$.data.follows[0].name").value(users.get(0).getName()))
                .andExpect(jsonPath("$.data.follows[1].id").value(users.get(1).getId()))
                .andDo(print());

        verify(followService, times(1)).follows(thor.getId());
    }

    @Test
    void shouldBeAbleToGiveNotFoundErrorResponseWhenFollowsUserIdDoesNotExists() throws Exception {
        when(followService.follows(thor.getId())).thenThrow(new UserNotFoundException("User not found!"));

        ResultActions result = mockMvc.perform(get("/users/{userId}/follows", thor.getId()));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("User not found!"))
                .andDo(print());

        verify(followService, times(1)).follows(thor.getId());
    }

    @Test
    void shouldBeAbleToUnfollowAUser() throws Exception {
        String requestJson = new ObjectMapper().writeValueAsString(followAndUnfollowRequestBodyContainsThor);

        ResultActions result = mockMvc.perform(post("/users/{userId}/unfollow", ironman.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("User Id 1 unfollowed User Id 2!"))
                .andDo(print());

        verify(followService, times(1)).unfollow(ironman.getId(), thor.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenBothFollowerIdAndFollowsIdAreSameToUnFollow() throws Exception {
        when(followService.unfollow(thor.getId(), thor.getId())).thenThrow(new UserIdsAreSameException("Both ids are same!"));
        String requestJson = new ObjectMapper().writeValueAsString(followAndUnfollowRequestBodyContainsThor);

        ResultActions result = mockMvc.perform(post("/users/{userId}/unfollow", thor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Both ids are same!"))
                .andDo(print());

        verify(followService, times(1)).unfollow(thor.getId(), thor.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenUserIsNotFollowingAnotherUser() throws Exception {
        when(followService.unfollow(ironman.getId(), thor.getId())).thenThrow(new UserNotFollowingException("User not following"));
        String requestJson = new ObjectMapper().writeValueAsString(followAndUnfollowRequestBodyContainsThor);

        ResultActions result = mockMvc.perform(post("/users/{userId}/unfollow", ironman.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("User not following"))
                .andDo(print());

        verify(followService, times(1)).unfollow(ironman.getId(), thor.getId());
    }
}