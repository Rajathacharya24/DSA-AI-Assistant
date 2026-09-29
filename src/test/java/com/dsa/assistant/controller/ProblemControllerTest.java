package com.dsa.assistant.controller;

import com.dsa.assistant.dto.CodeReviewResponse;
import com.dsa.assistant.dto.CodeSubmitRequest;
import com.dsa.assistant.dto.ProblemDTO;
import com.dsa.assistant.exception.ProblemNotFoundException;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.service.CodeReviewService;
import com.dsa.assistant.service.ProblemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProblemService problemService;

    @MockBean
    private CodeReviewService codeReviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllProblems() throws Exception {
        ProblemDTO dto = new ProblemDTO();
        dto.setId(1L);
        dto.setTitle("Two Sum");
        dto.setDifficulty(Difficulty.EASY);
        when(problemService.getAllProblems()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/problems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Two Sum"));
    }

    @Test
    void testGetProblemById_Success() throws Exception {
        ProblemDTO dto = new ProblemDTO();
        dto.setId(1L);
        dto.setTitle("Two Sum");
        when(problemService.getProblemById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/problems/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Two Sum"));
    }

    @Test
    void testGetProblemById_NotFound() throws Exception {
        when(problemService.getProblemById(99L)).thenThrow(new ProblemNotFoundException("Not found"));

        mockMvc.perform(get("/api/problems/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSubmitCode_Success() throws Exception {
        CodeSubmitRequest request = new CodeSubmitRequest();
        request.setCode("def func(): pass");

        CodeReviewResponse response = new CodeReviewResponse(true, "Good", "O(N)", "O(1)", "Nice");
        when(codeReviewService.reviewAndSubmitCode(eq(1L), any(), any())).thenReturn(response);

        // We use any() instead of eq(1L) because @WithMockUser might not have the correct ID
        mockMvc.perform(post("/api/problems/1/submit")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(
                            new com.dsa.assistant.security.CurrentUser(1L, "Test", "test@test.com", "pass", java.util.List.of())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true));
    }

    @Test
    void testSubmitCode_Unauthorized() throws Exception {
        CodeSubmitRequest request = new CodeSubmitRequest();
        request.setCode("def func(): pass");

        mockMvc.perform(post("/api/problems/1/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetProblemsByTopic() throws Exception {
        ProblemDTO dto = new ProblemDTO();
        dto.setId(1L);
        dto.setTitle("Two Sum");
        when(problemService.getProblemsByTopic("arrays")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/problems/topic/arrays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Two Sum"));
    }

    @Test
    void testGetProblemsByDifficulty() throws Exception {
        ProblemDTO dto = new ProblemDTO();
        dto.setId(1L);
        dto.setTitle("Two Sum");
        when(problemService.getProblemsByDifficulty("EASY")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/problems/difficulty/EASY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Two Sum"));
    }

    @Test
    @WithMockUser
    void testCreateProblem() throws Exception {
        com.dsa.assistant.dto.CreateProblemDTO request = new com.dsa.assistant.dto.CreateProblemDTO();
        request.setTitle("New Problem");
        request.setTopic("arrays");
        request.setDifficulty(Difficulty.MEDIUM);
        
        ProblemDTO dto = new ProblemDTO();
        dto.setId(2L);
        dto.setTitle("New Problem");

        when(problemService.createProblem(any(com.dsa.assistant.dto.CreateProblemDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/problems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Problem"));
    }

    @Test
    @WithMockUser
    void testCreateProblem_ValidationFailure() throws Exception {
        com.dsa.assistant.dto.CreateProblemDTO request = new com.dsa.assistant.dto.CreateProblemDTO();
        // Title missing, which violates @Valid

        mockMvc.perform(post("/api/problems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
