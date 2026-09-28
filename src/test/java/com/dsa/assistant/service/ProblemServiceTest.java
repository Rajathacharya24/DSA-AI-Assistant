package com.dsa.assistant.service;

import com.dsa.assistant.dto.CreateProblemDTO;
import com.dsa.assistant.dto.ProblemDTO;
import com.dsa.assistant.exception.ProblemNotFoundException;
import com.dsa.assistant.exception.TopicNotFoundException;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.Topic;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProblemServiceTest {

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private ProgressService progressService;

    @InjectMocks
    private ProblemService problemService;

    private Problem problem;
    private Topic topic;

    @BeforeEach
    void setUp() {
        topic = new Topic("ARRAYS");
        topic.setId(1L);

        problem = new Problem();
        problem.setId(1L);
        problem.setTitle("Two Sum");
        problem.setDescription("Find two numbers");
        problem.setDifficulty(Difficulty.EASY);
        problem.setTopic(topic);
        problem.setExplanation("Explanation");
        problem.setSolution("Solution");
    }

    @Test
    void testGetAllProblems() {
        when(problemRepository.findAll()).thenReturn(List.of(problem));

        List<ProblemDTO> result = problemService.getAllProblems();

        assertEquals(1, result.size());
        assertEquals("Two Sum", result.get(0).getTitle());
    }

    @Test
    void testGetProblemById_Success() {
        when(problemRepository.findById(1L)).thenReturn(Optional.of(problem));

        ProblemDTO result = problemService.getProblemById(1L);

        assertNotNull(result);
        assertEquals("Two Sum", result.getTitle());
    }

    @Test
    void testGetProblemById_NotFound() {
        when(problemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProblemNotFoundException.class, () -> problemService.getProblemById(1L));
    }

    @Test
    void testGetProblemsByTopic_Success() {
        when(topicRepository.findByName("ARRAYS")).thenReturn(Optional.of(topic));
        when(problemRepository.findByTopicId(1L)).thenReturn(List.of(problem));

        List<ProblemDTO> result = problemService.getProblemsByTopic("ARRAYS");

        assertEquals(1, result.size());
        assertEquals("Two Sum", result.get(0).getTitle());
    }

    @Test
    void testGetProblemsByTopic_NotFound() {
        when(topicRepository.findByName("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(TopicNotFoundException.class, () -> problemService.getProblemsByTopic("UNKNOWN"));
    }

    @Test
    void testGetProblemsByDifficulty_Success() {
        when(problemRepository.findByDifficulty(Difficulty.EASY)).thenReturn(List.of(problem));

        List<ProblemDTO> result = problemService.getProblemsByDifficulty("EASY");

        assertEquals(1, result.size());
    }

    @Test
    void testGetProblemsByDifficulty_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> problemService.getProblemsByDifficulty("INVALID"));
    }

    @Test
    void testCreateProblem() {
        CreateProblemDTO createDTO = new CreateProblemDTO();
        createDTO.setTitle("Two Sum");
        createDTO.setDescription("Find two numbers");
        createDTO.setTopic("ARRAYS");
        createDTO.setDifficulty(Difficulty.EASY);

        when(topicRepository.findByName("ARRAYS")).thenReturn(Optional.of(topic));
        when(problemRepository.save(any(Problem.class))).thenReturn(problem);

        ProblemDTO result = problemService.createProblem(createDTO);

        assertNotNull(result);
        assertEquals("Two Sum", result.getTitle());
        verify(problemRepository).save(any(Problem.class));
    }

    @Test
    void testGetHint() {
        when(problemRepository.findById(1L)).thenReturn(Optional.of(problem));
        doNothing().when(progressService).incrementHintsUsed(1L, 1L);

        String hint = problemService.getHint(1L, 1L, 1);

        assertTrue(hint.contains("Problem Title: Two Sum"));
        assertTrue(hint.contains("Hint Level 1"));
        verify(progressService).incrementHintsUsed(1L, 1L);
    }
}
