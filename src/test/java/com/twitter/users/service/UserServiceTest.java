package com.twitter.users.service;

import com.twitter.users.exceptions.UserNameNullException;
import com.twitter.users.repository.User;
import com.twitter.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
