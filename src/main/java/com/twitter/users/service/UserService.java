package com.twitter.users.service;

import com.twitter.users.exceptions.UserAlreadyExistException;
import com.twitter.users.exceptions.UserNameNullException;
import com.twitter.users.exceptions.UserNotFoundException;
import com.twitter.users.model.User;
import com.twitter.users.model.UserRepository;
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

        Optional<User> existedUser = userRepository.findById(user.getId());

        if (existedUser.isPresent())
            throw new UserAlreadyExistException("User is already exists with id = " + user.getId());

        return userRepository.save(user);
    }

    public User getById(int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) throw new UserNotFoundException("User not found with id = " + id);

        return user.get();
    }

    private static boolean isNameNull(User user) {
        return user.getName() == null;
    }
}
