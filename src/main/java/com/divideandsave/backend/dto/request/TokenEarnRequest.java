package com.divideandsave.backend.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class TokenEarnRequest {
    @Min(value = 1, message = "Token amount must be greater than 0")
    private int amount;
}
