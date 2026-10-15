package com.dsa.assistant.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StudyPlanResponse {
    private Long id;
    private Long userId;
    private String topic;
    private String difficulty;
    private String goal;
    private String weeklyTarget;
    private String expectedTime;
    private Integer progressPercentage;
    private List<String> studySteps;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getWeeklyTarget() {
        return weeklyTarget;
    }

    public void setWeeklyTarget(String weeklyTarget) {
        this.weeklyTarget = weeklyTarget;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public List<String> getStudySteps() {
        return studySteps;
    }

    public void setStudySteps(List<String> studySteps) {
        this.studySteps = studySteps;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
