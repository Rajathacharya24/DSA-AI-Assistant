package com.dsa.assistant.service;

import com.dsa.assistant.dto.StudyPlanRequest;
import com.dsa.assistant.dto.StudyPlanResponse;
import com.dsa.assistant.model.StudyPlan;
import com.dsa.assistant.model.User;
import com.dsa.assistant.repository.StudyPlanRepository;
import com.dsa.assistant.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyPlanService {

    private final StudyPlanRepository studyPlanRepository;
    private final UserRepository userRepository;

    public StudyPlanService(StudyPlanRepository studyPlanRepository, UserRepository userRepository) {
        this.studyPlanRepository = studyPlanRepository;
        this.userRepository = userRepository;
    }

    public StudyPlanResponse generatePlan(Long userId, StudyPlanRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        String topic = normalizeTopic(request.getTopic());
        String difficulty = normalizeDifficulty(request.getDifficulty());
        String goal = request.getGoal() != null && !request.getGoal().isBlank()
                ? request.getGoal()
                : "Strengthen " + topic + " fundamentals";

        String weeklyTarget = buildWeeklyTarget(difficulty);
        String expectedTime = buildExpectedTime(difficulty);
        List<String> studySteps = buildStudySteps(topic, difficulty);

        StudyPlan plan = new StudyPlan();
        plan.setUser(user);
        plan.setTopic(topic);
        plan.setDifficulty(difficulty);
        plan.setGoal(goal);
        plan.setWeeklyTarget(weeklyTarget);
        plan.setExpectedTime(expectedTime);
        plan.setProgressPercentage(0);
        plan.setLearningSequence(String.join(" | ", studySteps));

        StudyPlan savedPlan = studyPlanRepository.save(plan);
        return mapToResponse(savedPlan, studySteps);
    }

    public StudyPlanResponse getLatestPlan(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        StudyPlan plan = studyPlanRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new IllegalArgumentException("No study plan found for user id: " + userId));

        return mapToResponse(plan, parseStudySteps(plan.getLearningSequence()));
    }

    private String normalizeTopic(String topic) {
        if (topic == null || topic.isBlank()) {
            return "ARRAYS";
        }
        return topic.trim().toUpperCase();
    }

    private String normalizeDifficulty(String difficulty) {
        if (difficulty == null || difficulty.isBlank()) {
            return "EASY";
        }
        return difficulty.trim().toUpperCase();
    }

    private String buildWeeklyTarget(String difficulty) {
        if ("HARD".equals(difficulty)) {
            return "1 easy, 2 medium, 2 hard practice problems";
        }
        if ("MEDIUM".equals(difficulty)) {
            return "2 easy, 2 medium, 1 hard practice problems";
        }
        return "3 easy, 2 medium, 1 hard practice problems";
    }

    private String buildExpectedTime(String difficulty) {
        if ("HARD".equals(difficulty)) {
            return "3-4 hours per week";
        }
        if ("MEDIUM".equals(difficulty)) {
            return "2-3 hours per week";
        }
        return "1-2 hours per week";
    }

    private List<String> buildStudySteps(String topic, String difficulty) {
        return Arrays.asList(
                "Review the core concept behind " + topic,
                "Identify the pattern and constraints for this problem type",
                "Solve 2-3 examples at " + difficulty + " difficulty",
                "Check edge cases, complexity, and mistakes before submission"
        );
    }

    private List<String> parseStudySteps(String learningSequence) {
        if (learningSequence == null || learningSequence.isBlank()) {
            return List.of();
        }
        return Arrays.stream(learningSequence.split("\\|"))
                .map(String::trim)
                .filter(step -> !step.isEmpty())
                .collect(Collectors.toList());
    }

    private StudyPlanResponse mapToResponse(StudyPlan plan, List<String> steps) {
        StudyPlanResponse response = new StudyPlanResponse();
        response.setId(plan.getId());
        response.setUserId(plan.getUser() != null ? plan.getUser().getId() : null);
        response.setTopic(plan.getTopic());
        response.setDifficulty(plan.getDifficulty());
        response.setGoal(plan.getGoal());
        response.setWeeklyTarget(plan.getWeeklyTarget());
        response.setExpectedTime(plan.getExpectedTime());
        response.setProgressPercentage(plan.getProgressPercentage());
        response.setStudySteps(steps);
        response.setCreatedAt(plan.getCreatedAt());
        return response;
    }
}
