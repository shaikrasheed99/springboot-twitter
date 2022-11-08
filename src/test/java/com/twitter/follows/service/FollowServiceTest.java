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
    private User ironman;
    private User thor;
    private List<IUser> users;
    private FollowsCompositePrimaryKey ironmanThorKey;
    private Follow ironmanFollowsThor;

    @BeforeEach
    void setUp() {
        ironman = new User(1, "ironman");
        thor = new User(2, "Thor");
        users = new ArrayList<>();
        users.add(ironman);
        users.add(thor);
        ironmanThorKey = new FollowsCompositePrimaryKey(ironman, thor);
        ironmanFollowsThor = new Follow(ironmanThorKey);
    }

    @Test
    void shouldBeAbleToFollowAUser() {
        when(userService.getUserById(ironman.getId())).thenReturn(ironman);
        when(userService.getUserById(thor.getId())).thenReturn(thor);
        when(followRepository.save(any(Follow.class))).thenReturn(ironmanFollowsThor);

        Follow savedFollow = followService.follow(ironman.getId(), thor.getId());

        assertEquals(savedFollow.getFollowsCompositePrimaryKey().getFollower(), ironmanFollowsThor.getFollowsCompositePrimaryKey().getFollower());
        assertEquals(savedFollow.getFollowsCompositePrimaryKey().getFollows(), ironmanFollowsThor.getFollowsCompositePrimaryKey().getFollows());

        verify(userService, times(1)).getUserById(ironman.getId());
        verify(userService, times(1)).getUserById(thor.getId());
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenAUserIsAlreadyFollowingAnotherUser() {
        when(followRepository.findById(any(FollowsCompositePrimaryKey.class))).thenReturn(Optional.ofNullable(ironmanFollowsThor));

        assertThrows(UserAlreadyFollowingException.class, () -> followService.follow(ironman.getId(), thor.getId()));

        verify(followRepository, times(1)).findById(any(FollowsCompositePrimaryKey.class));
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowerIdDoesNotExists() {
        when(userService.getUserById(ironman.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.follow(ironman.getId(), thor.getId()));

        verify(userService, times(1)).getUserById(ironman.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowsIdDoesNotExists() {
        when(userService.getUserById(ironman.getId())).thenReturn(ironman);
        when(userService.getUserById(thor.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.follow(ironman.getId(), thor.getId()));

        verify(userService, times(1)).getUserById(ironman.getId());
        verify(userService, times(1)).getUserById(thor.getId());
    }

    @Test
    void shouldBeAbleToReturnFollowersOfAUser() {
        when(followRepository.findFollowers(ironman.getId())).thenReturn(users);

        List<IUser> listOfFollowers = followService.followers(ironman.getId());

        assertEquals(2, listOfFollowers.size());

        verify(followRepository, times(1)).findFollowers(ironman.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowerIdIsNotPresentInUsersTable() {
        when(userService.getUserById(ironman.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.followers(ironman.getId()));
    }

    @Test
    void shouldBeAbleToReturnFollowsOfAUser() {
        when(followRepository.findFollows(thor.getId())).thenReturn(users);

        List<IUser> listOfFollows = followService.follows(thor.getId());

        assertEquals(2, listOfFollows.size());

        verify(followRepository, times(1)).findFollows(thor.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenFollowsIdIsNotPresentInUsersTable() {
        when(userService.getUserById(thor.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> followService.follows(thor.getId()));
    }

    @Test
    void shouldBeAbleToUnfollowAUser() {
        when(userService.getUserById(ironman.getId())).thenReturn(ironman);
        when(userService.getUserById(thor.getId())).thenReturn(thor);
        when(followRepository.findById(any(FollowsCompositePrimaryKey.class))).thenReturn(Optional.ofNullable(ironmanFollowsThor));

        Follow unfollow = followService.unfollow(ironman.getId(), thor.getId());

        assertEquals(unfollow.getFollowsCompositePrimaryKey().getFollower(), ironman);
        assertEquals(unfollow.getFollowsCompositePrimaryKey().getFollows(), thor);

        verify(followRepository, times(1)).delete(any(Follow.class));
        verify(userService, times(1)).getUserById(ironman.getId());
        verify(userService, times(1)).getUserById(thor.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenUserIsNotFollowingAnotherUser() {
        when(userService.getUserById(ironman.getId())).thenReturn(ironman);
        when(userService.getUserById(thor.getId())).thenReturn(thor);
        when(followRepository.findById(any(FollowsCompositePrimaryKey.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFollowingException.class, () -> followService.unfollow(ironman.getId(), thor.getId()));

        verify(followRepository, times(1)).findById(any(FollowsCompositePrimaryKey.class));
        verify(userService, times(1)).getUserById(ironman.getId());
        verify(userService, times(1)).getUserById(thor.getId());
    }
}
