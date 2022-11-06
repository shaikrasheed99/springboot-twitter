package com.twitter.follows.service;

import com.twitter.follows.model.Follow;
import com.twitter.follows.model.FollowRepository;
import com.twitter.follows.model.FollowsCompositePrimaryKey;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.model.User;
import com.twitter.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private FollowService followService;
    private User follower;
    private User follows;
    private Follow follow;

    @BeforeEach
    void setUp() {
        follower = new User(1, "ironman");
        follows = new User(2, "Thor");
        FollowsCompositePrimaryKey primaryKey = new FollowsCompositePrimaryKey(follower, follows);
        follow = new Follow(primaryKey);
    }

    @Test
    void shouldBeAbleToFollowAUser() {
        when(userService.getUserById(follower.getId())).thenReturn(follower);
        when(userService.getUserById(follows.getId())).thenReturn(follows);
        when(followRepository.save(any(Follow.class))).thenReturn(follow);

        Follow savedFollow = followService.follow(follower.getId(), follows.getId());

        assertEquals(savedFollow.getFollowsCompositePrimaryKey().getFollower(), follow.getFollowsCompositePrimaryKey().getFollower());
        assertEquals(savedFollow.getFollowsCompositePrimaryKey().getFollows(), follow.getFollowsCompositePrimaryKey().getFollows());

        verify(userService, times(1)).getUserById(follower.getId());
        verify(userService, times(1)).getUserById(follows.getId());
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowerIdIsNotExists() {
        when(userService.getUserById(follower.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.follow(follower.getId(), follows.getId()));

        verify(userService, times(1)).getUserById(follower.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowsIdIsNotExists() {
        when(userService.getUserById(follower.getId())).thenReturn(follower);
        when(userService.getUserById(follows.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.follow(follower.getId(), follows.getId()));

        verify(userService, times(1)).getUserById(follower.getId());
        verify(userService, times(1)).getUserById(follows.getId());
    }
}
