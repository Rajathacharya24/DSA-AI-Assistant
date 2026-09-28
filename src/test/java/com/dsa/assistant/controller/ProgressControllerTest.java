package com.dsa.assistant.controller;

import com.dsa.assistant.dto.RecommendationResponse;
import com.dsa.assistant.dto.UserProgressDTO;
import com.dsa.assistant.service.ProgressService;
import com.dsa.assistant.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressService progressService;

    @MockBean
    private RecommendationService recommendationService;

    // We can use a custom mock user that has the required CurrentUser details, 
    // or simulate the JWT filter injecting the correct principal.
    // For simplicity, let's assume the user ID matches the path variable.

    @Test
    void testGetUserProgress_Success() throws Exception {
        // Need to provide custom user
        
        mockMvc.perform(get("/api/users/1/progress")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(
                    new com.dsa.assistant.security.CurrentUser(1L, "Test", "test@test.com", "pass", java.util.List.of())
                )))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserProgress_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/users/1/progress"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetRecommendations_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/users/1/recommendations"))
                .andExpect(status().isUnauthorized());
    }
}
