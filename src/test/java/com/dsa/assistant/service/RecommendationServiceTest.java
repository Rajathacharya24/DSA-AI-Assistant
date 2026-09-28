package com.dsa.assistant.service;

import com.dsa.assistant.dto.RecommendationResponse;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.Progress;
import com.dsa.assistant.model.Topic;
import com.dsa.assistant.model.User;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.model.enums.ProgressStatus;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.ProgressRepository;
import com.dsa.assistant.repository.TopicRepository;
import com.dsa.assistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private User user;
    private Topic topicArrays;
    private Topic topicStrings;
    private Problem problem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        topicArrays = new Topic("ARRAYS");
        topicStrings = new Topic("STRINGS");

        problem = new Problem();
        problem.setId(1L);
        problem.setTitle("Two Sum");
        problem.setTopic(topicArrays);
    }

    @Test
    void testGetRecommendation_NoTopics() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(topicRepository.findAll()).thenReturn(List.of());

        RecommendationResponse response = recommendationService.getRecommendation(1L);

        assertEquals("No topics available", response.getRecommendedTopic());
    }

    @Test
    void testGetRecommendation_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(topicRepository.findAll()).thenReturn(List.of(topicArrays, topicStrings));

        Progress p1 = new Progress();
        p1.setProblem(problem);
        p1.setStatus(ProgressStatus.SOLVED);
        when(progressRepository.findByUserId(1L)).thenReturn(List.of(p1));

        Problem recProblem = new Problem();
        recProblem.setId(2L);
        recProblem.setTitle("Reverse String");
        when(problemRepository.findByTopicNameIgnoreCaseAndDifficulty(eq("STRINGS"), any(Difficulty.class)))
                .thenReturn(List.of(recProblem));

        RecommendationResponse response = recommendationService.getRecommendation(1L);

        assertEquals("STRINGS", response.getRecommendedTopic());
        assertEquals("Reverse String", response.getNextProblemTitle());
        assertEquals(2L, response.getNextProblemId());
    }
}
