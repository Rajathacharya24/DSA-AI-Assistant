package com.dsa.assistant.controller;

import com.dsa.assistant.dto.UserProgressDTO;
import com.dsa.assistant.dto.RecommendationResponse;
import com.dsa.assistant.service.ProgressService;
import com.dsa.assistant.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class ProgressController {

    private final ProgressService progressService;
    private final RecommendationService recommendationService;

    public ProgressController(ProgressService progressService, RecommendationService recommendationService) {
        this.progressService = progressService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}/progress")
    public ResponseEntity<UserProgressDTO> getUserProgress(@PathVariable Long userId) {
        UserProgressDTO progress = progressService.getUserProgress(userId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable Long userId) {
        RecommendationResponse recommendation = recommendationService.getRecommendation(userId);
        return ResponseEntity.ok(recommendation);
    }
}
