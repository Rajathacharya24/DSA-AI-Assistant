package com.dsa.assistant.service;

import com.dsa.assistant.dto.PracticeSessionRequest;
import com.dsa.assistant.dto.PracticeSessionResponse;
import com.dsa.assistant.dto.PracticeValidationRequest;
import com.dsa.assistant.dto.PracticeValidationResponse;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.Topic;
import com.dsa.assistant.model.User;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.repository.PracticeSessionRepository;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PracticeServiceTest {

    @Mock
    private PracticeSessionRepository practiceSessionRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PracticeService practiceService;

    @Test
    void testStartPracticeSession() {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPasswordHash("hash");

        Topic topic = new Topic("ARRAYS");
        topic.setId(5L);

        Problem problem = new Problem();
        problem.setId(10L);
        problem.setTitle("Two Sum");
        problem.setDescription("Find pair");
        problem.setDifficulty(Difficulty.EASY);
        problem.setTopic(topic);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(problemRepository.findById(10L)).thenReturn(Optional.of(problem));
        when(practiceSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PracticeSessionRequest request = new PracticeSessionRequest();
        request.setProblemId(10L);

        PracticeSessionResponse response = practiceService.startPracticeSession(1L, request);

        assertNotNull(response);
        assertEquals(10L, response.getProblemId());
        assertTrue(response.getReasoningPrompt().contains("Think before coding"));
        assertTrue(response.getChecklist().size() >= 4);
    }

    @Test
    void testValidatePractice() {
        PracticeValidationRequest request = new PracticeValidationRequest();
        request.setApproach("Use hashmap");
        request.setConstraints("n up to 1000");
        request.setEdgeCases("empty array, single element");
        request.setComplexity("O(n)");

        PracticeValidationResponse response = practiceService.validatePractice(request);

        assertNotNull(response);
        assertTrue(response.getBestCase().contains("best"));
        assertTrue(response.getWorstCase().contains("worst"));
        assertTrue(response.getChecklist().size() >= 4);
    }
}
