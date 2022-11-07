package com.twitter.follows.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.follows.model.Follow;
import com.twitter.follows.service.FollowService;
import com.twitter.helpers.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{followerId}/follow")
    public ResponseEntity<?> follow(@PathVariable int followerId, @RequestBody FollowRequestBody followRequestBody) throws JsonProcessingException {
        Follow follow = followService.follow(followerId, followRequestBody.getFollowsId());
        followResponseBody.setFollowerId(follow.getFollowsCompositePrimaryKey().getFollower().getId());
        followResponseBody.setFollowsId(follow.getFollowsCompositePrimaryKey().getFollows().getId());
        successResponse.setData(followResponseBody);
        String responseJson = successResponse.convertToJson();
        return ResponseEntity.status(HttpStatus.OK).body(responseJson);
    }
}
