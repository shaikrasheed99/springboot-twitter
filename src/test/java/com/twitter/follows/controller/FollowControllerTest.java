package com.twitter.follows.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.follows.exceptions.UserAlreadyFollowingException;
import com.twitter.follows.model.Follow;
import com.twitter.follows.model.FollowsCompositePrimaryKey;
import com.twitter.follows.service.FollowService;
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

import static org.mockito.Mockito.*;
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

        ResultActions result = mockMvc.perform(post("/users/{followerId}/follow", follower.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson));

        result.andExpect(status().isOk()).andExpect(jsonPath("$.data.followerId").value(follower.getId())).andExpect(jsonPath("$.data.followsId").value(follows.getId())).andDo(print());

        verify(followService, times(1)).follow(follower.getId(), follows.getId());
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenUserIsAlreadyFollowingAnotherUser() throws Exception {
        when(followService.follow(follower.getId(), follows.getId())).thenThrow(new UserAlreadyFollowingException("User already following!"));
        String requestJson = new ObjectMapper().writeValueAsString(followRequestBody);

        ResultActions result = mockMvc.perform(post("/users/{followerId}/follow", follower.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson));

        result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error.message").value("User already following!")).andDo(print());

        verify(followService, times(1)).follow(follower.getId(), follows.getId());
    }
}
