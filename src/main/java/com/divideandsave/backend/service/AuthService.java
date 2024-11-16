package com.divideandsave.backend.service;

import com.divideandsave.backend.dto.request.AuthRequest;
import com.divideandsave.backend.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse authenticate(AuthRequest authRequest);

    String refreshToken(String refreshToken);
}
