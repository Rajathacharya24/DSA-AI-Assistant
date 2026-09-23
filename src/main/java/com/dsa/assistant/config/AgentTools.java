package com.dsa.assistant.config;

import com.dsa.assistant.dto.ProblemDTO;
import com.dsa.assistant.model.Progress;
import com.dsa.assistant.service.ProblemService;
import com.dsa.assistant.service.ProgressService;
import com.dsa.assistant.service.RecommendationService;
import com.dsa.assistant.dto.RecommendationResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AgentTools {

    private final ProblemService problemService;
    private final ProgressService progressService;
    private final RecommendationService recommendationService;

    public AgentTools(ProblemService problemService, ProgressService progressService, RecommendationService recommendationService) {
        this.problemService = problemService;
        this.progressService = progressService;
        this.recommendationService = recommendationService;
    }

    public record ProblemRequest(String topic, String difficulty) {}
    
    @Bean
    @Description("Get a DSA problem by topic (e.g. ARRAY, STRING, TREE) and difficulty (e.g. EASY, MEDIUM, HARD). If user doesn't specify a topic, pass null.")
    public Function<ProblemRequest, ProblemDTO> problemTool() {
        return request -> problemService.getProblem(request.topic(), request.difficulty());
    }

    public record HintRequest(Long userId, Long problemId, int hintLevel) {}
    
    @Bean
    @Description("Get a hint for a specific problem by problemId and hintLevel (1 for conceptual, 2 for specific, 3 for near-solution). Requires userId to track progress, default to 1 if not specified.")
    public Function<HintRequest, String> hintTool() {
        return request -> {
            Long userId = request.userId() != null ? request.userId() : 1L;
            return problemService.getHint(userId, request.problemId(), request.hintLevel());
        };
    }

    public record ProgressRequest(Long userId) {}
    
    @Bean
    @Description("Get the learning progress of a user by userId. Shows solved problems and attempts. If userId is not specified, use 1.")
    public Function<ProgressRequest, String> progressTool() {
        return request -> {
            Long userId = request.userId() != null ? request.userId() : 1L;
            List<Progress> progressList = progressService.getProgressByUserId(userId);
            if (progressList.isEmpty()) {
                return "No progress found for user ID " + userId;
            }
            return progressList.stream()
                .map(p -> "Problem ID: " + p.getProblem().getId() + " - " + p.getProblem().getTitle() + " | Status: " + p.getStatus())
                .collect(Collectors.joining("\n"));
        };
    }

    public record RecommendationRequest(Long userId) {}

    @Bean
    @Description("Get learning recommendations for a user based on their progress. Returns the recommended topic, next problem to solve, difficulty, and reasoning. If userId is not specified, use 1.")
    public Function<RecommendationRequest, String> recommendationTool() {
        return request -> {
            Long userId = request.userId() != null ? request.userId() : 1L;
            RecommendationResponse response = recommendationService.getRecommendation(userId);
            return String.format("Reasoning: %s\nRecommended Topic: %s\nRecommended Difficulty: %s\nNext Problem: %s (ID: %d)",
                    response.getReasoning(),
                    response.getRecommendedTopic(),
                    response.getRecommendedDifficulty(),
                    response.getNextProblemTitle(),
                    response.getNextProblemId());
        };
    }
}
