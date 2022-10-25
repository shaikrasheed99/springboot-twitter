package com.twitter.users.service;

import com.twitter.users.exceptions.UserNameNullException;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.repository.User;
import com.twitter.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Ironman");
    }

    @Test
    void shouldBeAbleToCreateUser() {
        when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.create(user);

        assertEquals("Ironman", savedUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenNameIsNull() {
        User nullUser = new User();

        assertThrows(UserNameNullException.class, () -> userService.create(nullUser));
    }

    @Test
    void shouldBeAbleToGetUserById() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        User user = userService.getUserById(userId);

        assertEquals("Ironman", user.getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldBeAbleToThrowExceptionIfUserIsNotPresent() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }
}
