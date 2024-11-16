package com.divideandsave.backend.controller;

import com.divideandsave.backend.dto.request.UserRegisterRequest;
import com.divideandsave.backend.dto.response.ApiResponse;
import com.divideandsave.backend.dto.response.UserResponse;
import com.divideandsave.backend.entity.User;
import com.divideandsave.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@RequestBody @Valid UserRegisterRequest request) {
        User user = userService.registerUser(request);
        UserResponse response = new UserResponse(
                user.getId(), user.getName(), user.getEmail(), user.getRole().name(), user.getStatus().name()
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", response));
    }

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(new ApiResponse<>(true, "User found by email successfully", userService.getUserByEmail(email)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "User fetched successfully", userService.getAllUsers()));
    }
}
