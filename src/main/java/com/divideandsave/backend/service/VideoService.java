package com.divideandsave.backend.service;

import com.divideandsave.backend.dto.request.VideoMetadataRequest;
import com.divideandsave.backend.dto.request.VideoStatusUpdateRequest;
import com.divideandsave.backend.dto.response.VideoResponse;

import java.util.List;

public interface VideoService {

    VideoResponse saveVideoMetadata(VideoMetadataRequest request);

    void updateProcessingStatus(Long videoId, VideoStatusUpdateRequest request);

    List<VideoResponse> getAllVideos();

    void spendTokensForVideoProcessing(Long videoId);
}
