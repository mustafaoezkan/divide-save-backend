package com.divideandsave.backend.controller;

import com.divideandsave.backend.config.jwt.JwtUtil;
import com.divideandsave.backend.dto.request.AuthRequest;
import com.divideandsave.backend.dto.response.ApiResponse;
import com.divideandsave.backend.dto.response.AuthResponse;
import com.divideandsave.backend.service.AuthService;
import com.divideandsave.backend.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(@RequestBody @Valid AuthRequest authRequest) {
        logger.info("Login attempt for email: {}", authRequest.getEmail());
        AuthResponse authResponse = authService.authenticate(authRequest);
        logger.info("Login successful for email: {}", authRequest.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", authResponse));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", newAccessToken));
    }
}

