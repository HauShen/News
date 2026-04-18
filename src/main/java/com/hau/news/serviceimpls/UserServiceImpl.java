package com.hau.news.serviceimpls;

import com.hau.news.models.UserProfile;
import com.hau.news.models.exceptions.UserNotFoundException;
import com.hau.news.repositories.UserRepository;
import com.hau.news.requestbodies.UserRequestBody;
import com.hau.news.requestbodies.UserUpdatedRequestBody;
import com.hau.news.responsebodies.UserResponseBody;
import com.hau.news.responsebodies.UserUpdatedResponseBody;
import com.hau.news.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserResponseBody createUser(UserRequestBody userRequestBody) {
        UserProfile newUserProfile = new UserProfile();
        String id = UUID.randomUUID().toString();
        newUserProfile.setUserId(id);
        newUserProfile.setName(userRequestBody.getName());
        newUserProfile.setAge(userRequestBody.getAge());
        newUserProfile.setRole(userRequestBody.getRole());
        newUserProfile.setEmail(id + "@news-app.local");
        newUserProfile.setPassword(passwordEncoder.encode("changeme"));
        logger.info("User created with id={}", newUserProfile.getUserId());
        logger.debug("User details: name={}, age={}, role={}",
                newUserProfile.getName(),
                newUserProfile.getAge(),
                newUserProfile.getRole());
        userRepository.save(newUserProfile);
        return new UserResponseBody(newUserProfile);
    }

    @Override
    public UserResponseBody getUserByIdOrThrow(String userId) {
        UserProfile userProfile = userRepository.findByUserId(userId);
        if (userProfile == null) {
            logger.warn("User with userId={} not found", userId);
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        return new UserResponseBody(userProfile);
    }

    @Override
    public UserUpdatedResponseBody updateUserDetailsById(String userId, UserUpdatedRequestBody userUpdatedRequestBody) {
        UserProfile currentUserProfile = userRepository.findByUserId(userId);
        if (currentUserProfile == null) {
            logger.warn("User with userId={} not found", userId);
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        currentUserProfile.setName(userUpdatedRequestBody.getName());
        currentUserProfile.setAge(userUpdatedRequestBody.getAge());
        currentUserProfile.setRole(userUpdatedRequestBody.getRole());
        userRepository.save(currentUserProfile);
        return new UserUpdatedResponseBody(currentUserProfile);
    }

    @Override
    public String deleteUserById(String userId) {
        userRepository.deleteById(userId);
        return "User with Id " + userId + " is deleted";
    }
}
