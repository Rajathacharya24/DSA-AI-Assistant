package com.dsa.assistant.config;

import com.dsa.assistant.dto.ProblemDTO;
import com.dsa.assistant.model.Progress;
import com.dsa.assistant.service.ProblemService;
import com.dsa.assistant.service.ProgressService;
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

    public AgentTools(ProblemService problemService, ProgressService progressService) {
        this.problemService = problemService;
        this.progressService = progressService;
    }

    public record ProblemRequest(String topic, String difficulty) {}
    
    @Bean
    @Description("Get a DSA problem by topic (e.g. ARRAY, STRING, TREE) and difficulty (e.g. EASY, MEDIUM, HARD). If user doesn't specify a topic, pass null.")
    public Function<ProblemRequest, ProblemDTO> problemTool() {
        return request -> problemService.getProblem(request.topic(), request.difficulty());
    }

    public record HintRequest(Long problemId, int hintLevel) {}
    
    @Bean
    @Description("Get a hint for a specific problem by problemId and hintLevel (1 for subtle, 2 for moderate, 3 for strong).")
    public Function<HintRequest, String> hintTool() {
        return request -> problemService.getHint(request.problemId(), request.hintLevel());
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
}
