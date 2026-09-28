package com.dsa.assistant.service;

import com.dsa.assistant.dto.UserProgressDTO;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.Progress;
import com.dsa.assistant.model.User;
import com.dsa.assistant.model.enums.ProgressStatus;
import com.dsa.assistant.repository.AttemptRepository;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.ProgressRepository;
import com.dsa.assistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private AttemptRepository attemptRepository;

    @InjectMocks
    private ProgressService progressService;

    private User user;
    private Problem problem;
    private Progress progress;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        problem = new Problem();
        problem.setId(1L);

        progress = new Progress();
        progress.setId(1L);
        progress.setUser(user);
        progress.setProblem(problem);
        progress.setStatus(ProgressStatus.IN_PROGRESS);
        progress.setHintsUsed(1);
    }

    @Test
    void testGetUserProgress_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(progressRepository.countProblemsAttempted(1L)).thenReturn(5L);
        when(progressRepository.countProblemsSolved(1L)).thenReturn(3L);
        when(progressRepository.sumHintsUsed(1L)).thenReturn(2L);
        when(progressRepository.countTopicsStudied(1L)).thenReturn(2L);
        when(progressRepository.countEasySolved(1L)).thenReturn(2L);
        when(progressRepository.countMediumSolved(1L)).thenReturn(1L);
        when(progressRepository.countHardSolved(1L)).thenReturn(0L);

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        when(attemptRepository.findAttemptDatesByUserId(1L)).thenReturn(List.of(yesterday));

        UserProgressDTO result = progressService.getUserProgress(1L);

        assertNotNull(result);
        assertEquals(5L, result.getProblemsAttempted());
        assertEquals(3L, result.getProblemsSolved());
        assertEquals(1, result.getCurrentStreak()); // streak of 1 since yesterday
    }

    @Test
    void testGetUserProgress_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> progressService.getUserProgress(1L));
    }

    @Test
    void testIncrementHintsUsed_ExistingProgress() {
        when(progressRepository.findByUserIdAndProblemId(1L, 1L)).thenReturn(Optional.of(progress));
        when(progressRepository.save(any(Progress.class))).thenReturn(progress);

        progressService.incrementHintsUsed(1L, 1L);

        assertEquals(2, progress.getHintsUsed());
        verify(progressRepository).save(progress);
    }

    @Test
    void testIncrementHintsUsed_NewProgress() {
        when(progressRepository.findByUserIdAndProblemId(1L, 1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(problemRepository.findById(1L)).thenReturn(Optional.of(problem));
        
        progressService.incrementHintsUsed(1L, 1L);

        verify(progressRepository).save(any(Progress.class));
    }

    @Test
    void testUpdateProgressStatus_Correct() {
        when(progressRepository.findByUserIdAndProblemId(1L, 1L)).thenReturn(Optional.of(progress));
        
        progressService.updateProgressStatus(1L, 1L, true);

        assertEquals(ProgressStatus.SOLVED, progress.getStatus());
        verify(progressRepository).save(progress);
    }
}
