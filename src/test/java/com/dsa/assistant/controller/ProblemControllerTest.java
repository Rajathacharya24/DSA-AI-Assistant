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
    @WithMockUser
    void testSubmitCode_Success() throws Exception {
        CodeSubmitRequest request = new CodeSubmitRequest();
        request.setCode("def func(): pass");
        request.setCode("def func(): pass");

        CodeReviewResponse response = new CodeReviewResponse(true, "Good", "O(N)", "O(1)", "Nice");
        when(codeReviewService.reviewAndSubmitCode(eq(1L), any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/problems/1/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true));
    }

    @Test
    void testSubmitCode_Unauthorized() throws Exception {
        CodeSubmitRequest request = new CodeSubmitRequest();
        request.setCode("def func(): pass");
        request.setCode("def func(): pass");

        mockMvc.perform(post("/api/problems/1/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
