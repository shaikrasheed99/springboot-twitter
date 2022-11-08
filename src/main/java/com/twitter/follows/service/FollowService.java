package com.twitter.follows.service;

import com.twitter.follows.exceptions.UserAlreadyFollowingException;
import com.twitter.follows.exceptions.UserIdsAreSame;
import com.twitter.follows.exceptions.UserNotFollowingException;
import com.twitter.follows.model.Follow;
import com.twitter.follows.model.FollowRepository;
import com.twitter.follows.model.FollowsCompositePrimaryKey;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.model.IUser;
import com.twitter.users.model.User;
import com.twitter.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (followerId == followsId) throw new UserIdsAreSame("User id " + followerId + " cannot follow itself!");

        User follower = getUserWith(followerId, "follower Id");
        User follows = getUserWith(followsId, "follows Id");

        FollowsCompositePrimaryKey primaryKey = new FollowsCompositePrimaryKey(follower, follows);

        if (isFollowing(primaryKey)) throw new UserAlreadyFollowingException("User is already following!");
        Follow follow = new Follow(primaryKey);

        return followRepository.save(follow);
    }

    public List<IUser> followers(int id) {
        userService.getById(id);
        return followRepository.findFollowers(id);
    }

    public List<IUser> follows(int id) {
        userService.getById(id);
        return followRepository.findFollows(id);
    }

    public Follow unfollow(int followerId, int followsId) {
        User follower = getUserWith(followerId, "follower Id");
        User follows = getUserWith(followsId, "follows Id");

        FollowsCompositePrimaryKey primaryKey = new FollowsCompositePrimaryKey(follower, follows);

        if (!isFollowing(primaryKey)) throw new UserNotFollowingException("User is not following!");
        Follow follow = new Follow(primaryKey);

        followRepository.delete(follow);
        return follow;
    }

    private boolean isFollowing(FollowsCompositePrimaryKey primaryKey) {
        Optional<Follow> follow = followRepository.findById(primaryKey);
        return follow.isPresent();
    }

    private User getUserWith(int id, String customMessage) {
        User user;
        try {
            user = userService.getById(id);
        } catch (Exception exception) {
            throw new UserNotFoundException("User is not found with that " + customMessage + ": " + id);
        }
        return user;
    }
}
