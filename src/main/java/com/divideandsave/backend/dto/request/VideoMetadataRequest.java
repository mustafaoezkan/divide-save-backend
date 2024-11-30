package com.divideandsave.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VideoMetadataRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    private int durationSeconds;

    @NotBlank(message = "Video URL cannot be blank")
    private String videoUrl;
}
