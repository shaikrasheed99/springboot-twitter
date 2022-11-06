package com.twitter.follows.service;

import com.twitter.follows.model.Follow;
import com.twitter.follows.model.FollowRepository;
import com.twitter.follows.model.FollowsCompositePrimaryKey;
import com.twitter.users.model.User;
import com.twitter.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    @Autowired
    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    public Follow follow(int followerId, int followsId) {
        User follower = userService.getUserById(followerId);
        User follows = userService.getUserById(followsId);
        FollowsCompositePrimaryKey primaryKey = new FollowsCompositePrimaryKey(follower, follows);
        Follow follow = new Follow(primaryKey);
        return followRepository.save(follow);
    }
}