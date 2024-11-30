package com.divideandsave.backend.service.impl;

import com.divideandsave.backend.dto.request.TokenEarnRequest;
import com.divideandsave.backend.entity.Token;
import com.divideandsave.backend.entity.Transaction;
import com.divideandsave.backend.entity.TransactionType;
import com.divideandsave.backend.entity.User;
import com.divideandsave.backend.exception.CustomException;
import com.divideandsave.backend.repository.TokenRepository;
import com.divideandsave.backend.repository.TransactionRepository;
import com.divideandsave.backend.repository.UserRepository;
import com.divideandsave.backend.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public void earnTokens(TokenEarnRequest request) {
        User currentUser = getCurrentAuthenticatedUser();

        Token tokens = tokenRepository.findByUserId(currentUser.getId())
                .orElse(new Token(null, currentUser, 0, null, null));

        tokens.setTokenCount(tokens.getTokenCount() + request.getAmount());
        tokens.setLastEarnedAt(LocalDateTime.now());

        tokenRepository.save(tokens);

        transactionRepository.save(new Transaction(
                null,
                currentUser,
                TransactionType.EARNED,
                request.getAmount(),
                "Tokens earned from watching ads",
                LocalDateTime.now()
        ));
    }

    private User getCurrentAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }
}
