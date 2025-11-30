package com.fitness.userservice.service;


import com.fitness.userservice.dto.UserRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.reposistory.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public UserResponse registerUser(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            User existingUser = userRepository.findByEmail(userRequest.getEmail());
            return mapToUserResponse(existingUser);
        }

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setKeycloakId(userRequest.getKeycloakId());
        user.setPassword(userRequest.getPassword());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    public UserResponse getUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;


    }

    public Boolean validateUser(String userId) {
        log.info("Validating User: {}", userId);
        return userRepository.existsById(userId);
    }

    public @Nullable Boolean validateKeycloakUser(String userId) {
        return userRepository.existsByKeycloakId(userId);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }
}
