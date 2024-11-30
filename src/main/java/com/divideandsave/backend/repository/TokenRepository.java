package com.divideandsave.backend.repository;

import com.divideandsave.backend.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUserId(Long userId);
}
