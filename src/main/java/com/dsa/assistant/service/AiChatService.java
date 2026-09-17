package com.dsa.assistant.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

    private final ChatClient chatClient;

    public AiChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are an AI DSA Study Assistant.\n\n" +
                        "Your job is to help beginners learn Data Structures and Algorithms.\n\n" +
                        "Rules:\n" +
                        "- Explain concepts in simple English.\n" +
                        "- Use small examples.\n" +
                        "- Prefer step-by-step explanations.\n" +
                        "- Do not immediately provide complete solutions to coding problems.\n" +
                        "- Give hints when requested.\n" +
                        "- Explain time and space complexity.\n" +
                        "- Help users improve their Java code.\n" +
                        "- Encourage learning rather than simply giving answers.")
                .build();
    }

    public String getChatResponse(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
