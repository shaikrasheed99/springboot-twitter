package com.twitter.users.service;

import com.twitter.users.exceptions.UserNameNullException;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.repository.User;
import com.twitter.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        if (isNameNull(user)) {
            throw new UserNameNullException("User name should not be empty!");
        }

        return userRepository.save(user);
    }

    private static boolean isNameNull(User user) {
        return user.getName() == null;
    }

    public User getUserById(int userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with that id!");
        }

        return user.get();
    }
}
