package com.divideandsave.backend.service;

import com.divideandsave.backend.entity.RefreshToken;
import com.divideandsave.backend.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);
}
