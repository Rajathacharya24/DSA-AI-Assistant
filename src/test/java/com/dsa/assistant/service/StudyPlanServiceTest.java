package com.dsa.assistant.service;

import com.dsa.assistant.dto.StudyPlanRequest;
import com.dsa.assistant.dto.StudyPlanResponse;
import com.dsa.assistant.model.User;
import com.dsa.assistant.repository.StudyPlanRepository;
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
class StudyPlanServiceTest {

    @Mock
    private StudyPlanRepository studyPlanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudyPlanService studyPlanService;

    @Test
    void testGeneratePlan() {
        User user = new User();
        user.setId(10L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPasswordHash("hash");

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(studyPlanRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        StudyPlanRequest request = new StudyPlanRequest();
        request.setTopic("ARRAYS");
        request.setDifficulty("MEDIUM");
        request.setGoal("Build array fundamentals");

        StudyPlanResponse response = studyPlanService.generatePlan(10L, request);

        assertNotNull(response);
        assertEquals("ARRAYS", response.getTopic());
        assertEquals("MEDIUM", response.getDifficulty());
        assertTrue(response.getWeeklyTarget().contains("2 medium"));
        assertTrue(response.getStudySteps().size() >= 3);
    }
}
