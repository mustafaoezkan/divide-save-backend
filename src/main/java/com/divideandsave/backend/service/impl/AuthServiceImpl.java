package com.divideandsave.backend.service.impl;

import com.divideandsave.backend.config.jwt.JwtUtil;
import com.divideandsave.backend.config.services.UserDetailsImpl;
import com.divideandsave.backend.dto.request.AuthRequest;
import com.divideandsave.backend.dto.response.AuthResponse;
import com.divideandsave.backend.entity.RefreshToken;
import com.divideandsave.backend.entity.User;
import com.divideandsave.backend.exception.CustomException;
import com.divideandsave.backend.repository.RefreshTokenRepository;
import com.divideandsave.backend.repository.UserRepository;
import com.divideandsave.backend.service.AuthService;
import com.divideandsave.backend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        String accessToken = jwtUtil.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Override
    public String refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenService.verifyExpiration(
                refreshTokenRepository.findByToken(refreshToken)
                        .orElseThrow(() -> new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED))
        );

        return jwtUtil.generateToken(new UserDetailsImpl(token.getUser()));
    }
}
