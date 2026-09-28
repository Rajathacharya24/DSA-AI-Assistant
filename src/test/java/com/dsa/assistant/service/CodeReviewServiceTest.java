package com.dsa.assistant.service;

import com.dsa.assistant.dto.CodeReviewResponse;
import com.dsa.assistant.dto.CodeSubmitRequest;
import com.dsa.assistant.model.Attempt;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.User;
import com.dsa.assistant.repository.AttemptRepository;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodeReviewServiceTest {

    @Mock
    private AgentService agentService;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttemptRepository attemptRepository;

    @Mock
    private ProgressService progressService;

    @InjectMocks
    private CodeReviewService codeReviewService;

    @Test
    void testReviewAndSubmitCode() {
        Problem problem = new Problem();
        problem.setId(1L);
        problem.setTitle("Two Sum");
        problem.setDescription("Find two numbers");

        User user = new User();
        user.setId(1L);

        CodeSubmitRequest request = new CodeSubmitRequest();
        request.setCode("def twoSum(): return [0, 1]");
        request.setLanguage("python");

        CodeReviewResponse aiResponse = new CodeReviewResponse("Looks good", true);

        when(problemRepository.findById(1L)).thenReturn(Optional.of(problem));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(agentService.reviewCode("Two Sum", "Find two numbers", "def twoSum(): return [0, 1]"))
                .thenReturn(aiResponse);

        CodeReviewResponse response = codeReviewService.reviewAndSubmitCode(1L, 1L, request);

        assertTrue(response.isCorrect());
        assertEquals("Looks good", response.getFeedback());

        verify(attemptRepository).save(any(Attempt.class));
        verify(progressService).updateProgressStatus(1L, 1L, true);
    }
}
