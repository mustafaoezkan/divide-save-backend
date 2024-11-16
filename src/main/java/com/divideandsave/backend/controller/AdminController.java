package com.divideandsave.backend.controller;

import com.divideandsave.backend.dto.response.ApiResponse;
import com.divideandsave.backend.dto.response.UserResponse;
import com.divideandsave.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(user -> new UserResponse(
                        user.getId(), user.getName(), user.getEmail(), user.getRole().name(), user.getStatus().name()
                ))
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "All users retrieved successfully", users));
    }
}
