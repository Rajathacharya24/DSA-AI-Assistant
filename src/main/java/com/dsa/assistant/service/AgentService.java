package com.dsa.assistant.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final ChatClient chatClient;

    public AgentService(ChatClient.Builder chatClientBuilder) {
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
                        "- Encourage learning rather than simply giving answers.\n" +
                        "- If the user asks for a problem, call the getProblem tool and return the problem details.\n" +
                        "- If the user asks for a hint, call the getHint tool.\n" +
                        "- If the user asks for their progress, call the getProgress tool. (Default userId is 1 if not specified).\n")
                .defaultFunctions("getProblem", "getHint", "getProgress")
                .build();
    }

    public String getChatResponse(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
