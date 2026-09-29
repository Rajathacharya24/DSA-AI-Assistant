package com.dsa.assistant.service;

import com.dsa.assistant.dto.CodeReviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgentServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient.Builder chatClientBuilder;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    private AgentService agentService;

    @BeforeEach
    void setUp() {
        when(chatClientBuilder.defaultSystem(anyString())).thenReturn(chatClientBuilder);
        when(chatClientBuilder.defaultFunctions(anyString(), anyString(), anyString(), anyString())).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClient);

        agentService = new AgentService(chatClientBuilder);
    }

    @Test
    void testGetChatResponse() {
        String expectedResponse = "Hello there!";
        when(chatClient.prompt().user(anyString()).call().content()).thenReturn(expectedResponse);

        String response = agentService.getChatResponse("Hi");

        assertEquals(expectedResponse, response);
    }

    @Test
    void testReviewCode() {
        CodeReviewResponse mockResponse = new CodeReviewResponse();
        mockResponse.setCorrect(true);
        mockResponse.setFeedback("Good job!");

        when(chatClient.prompt().user(anyString()).call().entity(CodeReviewResponse.class)).thenReturn(mockResponse);

        CodeReviewResponse response = agentService.reviewCode("Two Sum", "Find two numbers", "def sum(): pass");

        assertNotNull(response);
        assertEquals(true, response.isCorrect());
        assertEquals("Good job!", response.getFeedback());
    }
}
