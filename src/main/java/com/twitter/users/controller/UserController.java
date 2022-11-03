package com.twitter.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.helpers.SuccessResponse;
import com.twitter.users.model.User;
import com.twitter.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    private SuccessResponse successResponse;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) throws JsonProcessingException {
        User savedUser = userService.create(user);
        successResponse.setData(Collections.singletonMap("id", savedUser.getId()));
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable int userId) throws JsonProcessingException {
        User user = userService.getUserById(userId);
        successResponse.setData(user);
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.OK).body(responseJson);
    }
}
