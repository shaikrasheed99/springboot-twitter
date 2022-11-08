package com.twitter.follows.service;

import com.twitter.follows.exceptions.UserAlreadyFollowingException;
import com.twitter.follows.exceptions.UserNotFollowingException;
import com.twitter.follows.model.Follow;
import com.twitter.follows.model.FollowRepository;
import com.twitter.follows.model.FollowsCompositePrimaryKey;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.model.IUser;
import com.twitter.users.model.User;
import com.twitter.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private FollowsCompositePrimaryKey primaryKey;

    @BeforeEach
    void setUp() {
        follower = new User(1, "ironman");
        follows = new User(2, "Thor");
        primaryKey = new FollowsCompositePrimaryKey(follower, follows);
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
    void shouldBeAbleToThrowExceptionWhenAUserIsAlreadyFollowingAnotherUser() {
        when(followRepository.findById(any(FollowsCompositePrimaryKey.class))).thenReturn(Optional.ofNullable(follow));

        assertThrows(UserAlreadyFollowingException.class, () -> followService.follow(follower.getId(), follows.getId()));

        verify(followRepository, times(1)).findById(any(FollowsCompositePrimaryKey.class));
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowerIdDoesNotExists() {
        when(userService.getUserById(follower.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.follow(follower.getId(), follows.getId()));

        verify(userService, times(1)).getUserById(follower.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowsIdDoesNotExists() {
        when(userService.getUserById(follower.getId())).thenReturn(follower);
        when(userService.getUserById(follows.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.follow(follower.getId(), follows.getId()));

        verify(userService, times(1)).getUserById(follower.getId());
        verify(userService, times(1)).getUserById(follows.getId());
    }

    @Test
    void shouldBeAbleToReturnFollowersOfAUser() {
        ArrayList<IUser> followers = new ArrayList<>();
        followers.add(follows);
        when(followRepository.findFollowers(follower.getId())).thenReturn(followers);

        List<IUser> listOfFollowers = followService.followers(follower.getId());

        assertEquals(1, listOfFollowers.size());

        verify(followRepository, times(1)).findFollowers(follower.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowerIdIsNotPresentInUsersTable() {
        when(userService.getUserById(follower.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.followers(follower.getId()));
    }

    @Test
    void shouldBeAbleToReturnFollowsOfAUser() {
        ArrayList<IUser> followings = new ArrayList<>();
        followings.add(follower);
        when(followRepository.findFollows(follows.getId())).thenReturn(followings);

        List<IUser> listOfFollows = followService.follows(follows.getId());

        assertEquals(1, listOfFollows.size());

        verify(followRepository, times(1)).findFollows(follows.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowsIdIsNotPresentInUsersTable() {
        when(userService.getUserById(follows.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.follows(follows.getId()));
    }

    @Test
    void shouldBeAbleToUnfollowAUser() {
        when(userService.getUserById(follower.getId())).thenReturn(follower);
        when(userService.getUserById(follows.getId())).thenReturn(follows);
        when(followRepository.findById(any(FollowsCompositePrimaryKey.class))).thenReturn(Optional.ofNullable(follow));

        followService.unfollow(follower.getId(), follows.getId());

        verify(followRepository, times(1)).delete(any(Follow.class));
        verify(userService, times(1)).getUserById(follower.getId());
        verify(userService, times(1)).getUserById(follows.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenUserIsNotFollowingAnotherUser() {
        when(userService.getUserById(follower.getId())).thenReturn(follower);
        when(userService.getUserById(follows.getId())).thenReturn(follows);
        when(followRepository.findById(any(FollowsCompositePrimaryKey.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFollowingException.class, () -> followService.unfollow(follower.getId(), follows.getId()));

        verify(followRepository, times(1)).findById(any(FollowsCompositePrimaryKey.class));
        verify(userService, times(1)).getUserById(follower.getId());
        verify(userService, times(1)).getUserById(follows.getId());
    }
}
