package com.twitter.follows.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.follows.model.Follow;
import com.twitter.follows.service.FollowService;
import com.twitter.helpers.SuccessResponse;
import com.twitter.users.model.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class FollowController {
    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @Autowired
    private SuccessResponse successResponse;

    @Autowired
    private FollowResponseBody followResponseBody;
    @Autowired
    private FollowersResponseBody followersResponseBody;
    @Autowired
    private FollowsResponseBody followsResponseBody;

    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> follow(@PathVariable int userId, @RequestBody FollowAndUnfollowRequestBody followAndUnfollowRequestBody) throws JsonProcessingException {
        Follow follow = followService.follow(userId, followAndUnfollowRequestBody.getFollowsId());
        followResponseBody.setFollowerId(follow.getFollowsCompositePrimaryKey().getFollower().getId());
        followResponseBody.setFollowsId(follow.getFollowsCompositePrimaryKey().getFollows().getId());
        successResponse.setData(followResponseBody);
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.OK).body(responseJson);
    }

    @GetMapping("/{userId}/followers")
    private ResponseEntity<?> followers(@PathVariable int userId) throws JsonProcessingException {
        List<IUser> followers = followService.followers(userId);
        followersResponseBody.setCount(followers.size());
        followersResponseBody.setFollowers(followers);
        successResponse.setData(followersResponseBody);
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.OK).body(responseJson);
    }

    @GetMapping("/{userId}/follows")
    private ResponseEntity<?> follows(@PathVariable int userId) throws JsonProcessingException {
        List<IUser> follows = followService.follows(userId);
        followsResponseBody.setCount(follows.size());
        followsResponseBody.setFollows(follows);
        successResponse.setData(followsResponseBody);
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.OK).body(responseJson);
    }

    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<?> unfollow(@PathVariable int userId, @RequestBody FollowAndUnfollowRequestBody followAndUnfollowRequestBody) throws JsonProcessingException {
        Follow unfollow = followService.unfollow(userId, followAndUnfollowRequestBody.getFollowsId());
        String message = "User Id = " + userId + " unfollowed User Id = " + followAndUnfollowRequestBody.getFollowsId();
        successResponse.setData(Collections.singletonMap("message", message));
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.OK).body(responseJson);
    }
}
