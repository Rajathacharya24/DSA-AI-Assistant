package com.dsa.assistant.dto;

import com.dsa.assistant.model.enums.Difficulty;

public class ProblemDTO {
    private Long id;
    private String title;
    private String topic;
    private Difficulty difficulty;
    private String description;
    
    // Add additional fields if needed, like exampleInput, exampleOutput, etc.

    public ProblemDTO() {
    }

    public ProblemDTO(Long id, String title, String topic, Difficulty difficulty, String description) {
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.difficulty = difficulty;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
