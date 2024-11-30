package com.divideandsave.backend.service.impl;

import com.divideandsave.backend.dto.request.VideoMetadataRequest;
import com.divideandsave.backend.dto.request.VideoStatusUpdateRequest;
import com.divideandsave.backend.dto.response.VideoResponse;
import com.divideandsave.backend.entity.*;
import com.divideandsave.backend.exception.CustomException;
import com.divideandsave.backend.repository.TokenRepository;
import com.divideandsave.backend.repository.TransactionRepository;
import com.divideandsave.backend.repository.UserRepository;
import com.divideandsave.backend.repository.VideoRepository;
import com.divideandsave.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final TokenRepository tokenRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public VideoResponse saveVideoMetadata(VideoMetadataRequest request) {
        User currentUser = getCurrentAuthenticatedUser();

        Video video = new Video();
        video.setUser(currentUser);
        video.setTitle(request.getTitle());
        video.setVideoUrl(request.getVideoUrl());
        video.setDescription(request.getDescription());
        video.setDurationSeconds(request.getDurationSeconds());
        video.setProcessed(false);

        Video savedVideo = videoRepository.save(video);

        return new VideoResponse(
                savedVideo.getId(),
                savedVideo.getTitle(),
                savedVideo.getVideoUrl(),
                savedVideo.getDescription(),
                savedVideo.getDurationSeconds(),
                savedVideo.isProcessed(),
                savedVideo.getCreatedAt()
        );
    }

    @Override
    public void updateProcessingStatus(Long videoId, VideoStatusUpdateRequest request) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new CustomException("Video not found", HttpStatus.NOT_FOUND));

        User currentUser = getCurrentAuthenticatedUser();

        if (!currentUser.getRole().equals(Role.ROLE_ADMIN) && !video.getUser().getId().equals(currentUser.getId())) {
            throw new CustomException("You do not have permission to update this video", HttpStatus.FORBIDDEN);
        }

        switch (request.getStatus().toLowerCase()) {
            case "processing":
                video.setProcessed(false);
                break;
            case "completed":
                video.setProcessed(true);
                video.setProcessedAt(LocalDateTime.now());
                break;
            case "failed":
                video.setProcessed(false);
                break;
            default:
                throw new CustomException("Invalid status", HttpStatus.BAD_REQUEST);
        }

        videoRepository.save(video);
    }

    @Override
    public List<VideoResponse> getAllVideos() {
        User currentUser = getCurrentAuthenticatedUser();
        List<Video> videos = videoRepository.findByUserId(currentUser.getId());

        return videos.stream().map(video -> new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getVideoUrl(),
                video.getDurationSeconds(),
                video.isProcessed(),
                video.getCreatedAt()
        )).toList();
    }

    @Override
    public void spendTokensForVideoProcessing(Long videoId) {
        User currentUser = getCurrentAuthenticatedUser();

        Token tokens = tokenRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new CustomException("No tokens found for user", HttpStatus.NOT_FOUND));

        int requiredTokens = 10;

        if (tokens.getTokenCount() < requiredTokens) {
            throw new CustomException("Not enough tokens", HttpStatus.BAD_REQUEST);
        }

        tokens.setTokenCount(tokens.getTokenCount() - requiredTokens);
        tokens.setLastUsedAt(LocalDateTime.now());

        tokenRepository.save(tokens);

        transactionRepository.save(new Transaction(
                null,
                currentUser,
                TransactionType.SPENT,
                requiredTokens,
                "Tokens spent for video processing",
                LocalDateTime.now()
        ));
    }

    private User getCurrentAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }
}
