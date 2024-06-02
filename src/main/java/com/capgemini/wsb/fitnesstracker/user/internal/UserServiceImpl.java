package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    };

    @Override
    public User updateUser(Long userId, User userUpdates) {
        userUpdates.setId(userId);
        return userRepository.save(userUpdates);
    };

    @Override
    public List<User> getUsersByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User>  getUsersOlderThan(String age){
        return userRepository.getUsersOlderThan(age);
    };

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}