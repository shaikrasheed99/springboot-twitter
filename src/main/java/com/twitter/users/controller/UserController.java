package com.twitter.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.users.helpers.UserSuccessResponse;
import com.twitter.users.repository.User;
import com.twitter.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    private UserSuccessResponse userSuccessResponse;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) throws JsonProcessingException {
        User savedUser = userService.create(user);
        userSuccessResponse.setData(Collections.singletonMap("id", savedUser.getId()));
        String responseJson = userSuccessResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    public void getUserById(int userId) {
        User user = userService.getUserById(userId);
    }
}
