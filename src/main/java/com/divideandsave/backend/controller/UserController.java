package com.divideandsave.backend.controller;

import com.divideandsave.backend.dto.request.UpdatePasswordRequest;
import com.divideandsave.backend.dto.request.UserRegisterRequest;
import com.divideandsave.backend.dto.response.ApiResponse;
import com.divideandsave.backend.dto.response.UserResponse;
import com.divideandsave.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@RequestBody @Valid UserRegisterRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", response));
    }

    @PutMapping("/update-password")
    public ResponseEntity<ApiResponse<String>> updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        userService.updatePassword(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password updated successfully", null));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteAccount() {
        userService.deleteAccount();
        return ResponseEntity.ok(new ApiResponse<>(true, "Account deleted successfully", null));
    }
}
