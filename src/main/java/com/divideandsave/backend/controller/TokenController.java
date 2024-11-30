package com.divideandsave.backend.controller;

import com.divideandsave.backend.dto.request.TokenEarnRequest;
import com.divideandsave.backend.dto.response.ApiResponse;
import com.divideandsave.backend.service.TokenService;
import com.divideandsave.backend.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/earn")
    public ResponseEntity<ApiResponse<String>> earnTokens(@RequestBody @Valid TokenEarnRequest request) {
        tokenService.earnTokens(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tokens earned successfully", null));
    }
}
