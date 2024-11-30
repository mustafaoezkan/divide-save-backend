package com.divideandsave.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VideoResponse {
    private Long id;
    private String title;
    private String videoUrl;
    private String description;
    private int durationSeconds;
    private boolean processed;
    private LocalDateTime createdAt;
}
