package com.divideandsave.backend.service;

import com.divideandsave.backend.dto.request.TokenEarnRequest;

public interface TokenService {

    void earnTokens(TokenEarnRequest request);
}
