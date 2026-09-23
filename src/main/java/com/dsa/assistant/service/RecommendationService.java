package com.dsa.assistant.service;

import com.dsa.assistant.dto.RecommendationResponse;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.Progress;
import com.dsa.assistant.model.Topic;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.model.enums.ProgressStatus;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.ProgressRepository;
import com.dsa.assistant.repository.TopicRepository;
import com.dsa.assistant.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final ProgressRepository progressRepository;
    private final ProblemRepository problemRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public RecommendationService(ProgressRepository progressRepository, ProblemRepository problemRepository, TopicRepository topicRepository, UserRepository userRepository) {
        this.progressRepository = progressRepository;
        this.problemRepository = problemRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public RecommendationResponse getRecommendation(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<Progress> progresses = progressRepository.findByUserId(userId);
        List<Topic> allTopics = topicRepository.findAll();

        Map<String, Long> solvedByTopic = progresses.stream()
                .filter(p -> p.getStatus() == ProgressStatus.SOLVED)
                .collect(Collectors.groupingBy(
                        p -> p.getProblem().getTopic().getName(),
                        Collectors.counting()
                ));

        // Fill missing topics with 0
        for (Topic topic : allTopics) {
            solvedByTopic.putIfAbsent(topic.getName(), 0L);
        }

        String strongestTopic = null;
        long maxSolved = -1;
        String recommendedTopic = null;
        long minSolved = Long.MAX_VALUE;

        for (Map.Entry<String, Long> entry : solvedByTopic.entrySet()) {
            if (entry.getValue() > maxSolved) {
                maxSolved = entry.getValue();
                strongestTopic = entry.getKey();
            }
            if (entry.getValue() < minSolved) {
                minSolved = entry.getValue();
                recommendedTopic = entry.getKey();
            }
        }

        if (recommendedTopic == null && !allTopics.isEmpty()) {
            recommendedTopic = allTopics.get(0).getName();
        } else if (recommendedTopic == null) {
            return new RecommendationResponse("No topics available", null, null, null, "There are no topics in the system.");
        }

        Difficulty recommendedDifficulty = Difficulty.EASY;
        if (minSolved >= 5) {
            recommendedDifficulty = Difficulty.HARD;
        } else if (minSolved >= 2) {
            recommendedDifficulty = Difficulty.MEDIUM;
        }

        List<Problem> recommendedProblems = problemRepository.findByTopicNameIgnoreCaseAndDifficulty(recommendedTopic, recommendedDifficulty);
        
        List<Long> solvedProblemIds = progresses.stream()
                .filter(p -> p.getStatus() == ProgressStatus.SOLVED)
                .map(p -> p.getProblem().getId())
                .collect(Collectors.toList());

        Problem nextProblem = recommendedProblems.stream()
                .filter(p -> !solvedProblemIds.contains(p.getId()))
                .findFirst()
                .orElse(null);

        // Fallback if no problems found
        if (nextProblem == null) {
             recommendedProblems = problemRepository.findByTopicNameIgnoreCaseAndDifficulty(recommendedTopic, Difficulty.EASY);
             nextProblem = recommendedProblems.stream()
                .filter(p -> !solvedProblemIds.contains(p.getId()))
                .findFirst()
                .orElse(null);
        }

        String reasoning;
        if (strongestTopic != null && maxSolved > 0) {
            if (strongestTopic.equals(recommendedTopic)) {
                reasoning = "You are doing well in " + strongestTopic + ". Keep practicing!";
            } else {
                reasoning = "Your " + strongestTopic + " fundamentals are strong (" + maxSolved + " solved). I recommend learning " + recommendedTopic + " next.";
            }
        } else {
            reasoning = "You are just getting started. I recommend beginning with " + recommendedTopic + ".";
        }

        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendedTopic(recommendedTopic);
        response.setRecommendedDifficulty(recommendedDifficulty.name());
        response.setReasoning(reasoning);
        if (nextProblem != null) {
            response.setNextProblemId(nextProblem.getId());
            response.setNextProblemTitle(nextProblem.getTitle());
        }

        return response;
    }
}
