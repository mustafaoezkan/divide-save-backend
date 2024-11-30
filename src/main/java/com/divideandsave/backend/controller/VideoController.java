package com.divideandsave.backend.controller;

import com.divideandsave.backend.dto.request.VideoMetadataRequest;
import com.divideandsave.backend.dto.request.VideoStatusUpdateRequest;
import com.divideandsave.backend.dto.response.ApiResponse;
import com.divideandsave.backend.dto.response.VideoResponse;
import com.divideandsave.backend.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VideoResponse>>> getAllVideos() {
        List<VideoResponse> videos = videoService.getAllVideos();
        return ResponseEntity.ok(new ApiResponse<>(true, "Videos retrieved successfully", videos));
    }

    @PostMapping("/metadata")
    public ResponseEntity<ApiResponse<VideoResponse>> uploadVideoMetadata(@RequestBody @Valid VideoMetadataRequest request) {
        VideoResponse response = videoService.saveVideoMetadata(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Video metadata saved successfully", response));
    }

    @PatchMapping("/{videoId}/status")
    public ResponseEntity<ApiResponse<String>> updateVideoProcessingStatus(
            @PathVariable Long videoId,
            @RequestBody @Valid VideoStatusUpdateRequest request) {
        videoService.updateProcessingStatus(videoId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Video processing status updated", null));
    }

    @PostMapping("/{videoId}/spend-tokens")
    public ResponseEntity<ApiResponse<String>> spendTokensForProcessing(@PathVariable Long videoId) {
        videoService.spendTokensForVideoProcessing(videoId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tokens spent successfully for video processing", null));
    }
}
