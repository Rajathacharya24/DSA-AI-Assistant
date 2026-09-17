package com.dsa.assistant.controller;

import com.dsa.assistant.dto.ChatRequest;
import com.dsa.assistant.dto.ChatResponse;
import com.dsa.assistant.service.AiChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String response = aiChatService.getChatResponse(request.getMessage());
        return new ChatResponse(response);
    }
}
