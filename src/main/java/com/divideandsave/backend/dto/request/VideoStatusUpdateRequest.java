package com.divideandsave.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VideoStatusUpdateRequest {
    @NotBlank(message = "Status cannot be blank")
    private String status; // "processing", "completed", "failed"
}
