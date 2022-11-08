package com.twitter.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.users.exceptions.UserAlreadyExistException;
import com.twitter.users.exceptions.UserNameNullException;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.model.User;
import com.twitter.users.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Ironman");
    }

    @Test
    void shouldBeAbleToCreateUser() throws Exception {
        when(userService.create(any(User.class))).thenReturn(user);
        String userJson = new ObjectMapper().writeValueAsString(user);

        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(0))
                .andDo(print());

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenNameIsNull() throws Exception {
        when(userService.create(any(User.class))).thenThrow(new UserNameNullException("User name exception"));
        User user = new User();
        String userJson = new ObjectMapper().writeValueAsString(user);

        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("User name exception"))
                .andDo(print());

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void shouldBeAbleToGiveBadRequestResponseWhenUserIdIsAlreadyExists() throws Exception {
        when(userService.create(any(User.class))).thenThrow(new UserAlreadyExistException("User already exists"));
        String userJson = new ObjectMapper().writeValueAsString(user);

        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("User already exists"))
                .andDo(print());

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void shouldBeAbleToGetUserDetailsWhenIdIsGiven() throws Exception {
        int userId = user.getId();
        when(userService.getById(userId)).thenReturn(user);

        ResultActions result = mockMvc.perform(get("/users/{userId}", userId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andDo(print());

        verify(userService, times(1)).getById(userId);
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenUserIsNotPresentWithGivenId() throws Exception {
        int userId = 1;
        when(userService.getById(userId)).thenThrow(new UserNotFoundException("User not found!"));

        ResultActions result = mockMvc.perform(get("/users/{userId}", userId));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("User not found!"))
                .andDo(print());

        verify(userService, times(1)).getById(userId);
    }
}
