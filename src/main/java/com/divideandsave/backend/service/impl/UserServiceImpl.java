package com.divideandsave.backend.service.impl;

import com.divideandsave.backend.dto.request.UpdatePasswordRequest;
import com.divideandsave.backend.dto.request.UserRegisterRequest;
import com.divideandsave.backend.dto.response.UserResponse;
import com.divideandsave.backend.entity.Role;
import com.divideandsave.backend.entity.Status;
import com.divideandsave.backend.entity.User;
import com.divideandsave.backend.exception.CustomException;
import com.divideandsave.backend.repository.UserRepository;
import com.divideandsave.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setStatus(Status.ACTIVE);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(), savedUser.getName(), savedUser.getEmail(),
                savedUser.getRole().name(), savedUser.getStatus().name()
        );
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        User currentUser = getCurrentAuthenticatedUser();

        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
            throw new CustomException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }

    @Override
    public void deleteAccount() {
        User currentUser = getCurrentAuthenticatedUser();
        userRepository.delete(currentUser);
    }

    private User getCurrentAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }
}
