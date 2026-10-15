package com.dsa.assistant.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_plans")
public class StudyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private String weeklyTarget;

    @Column(nullable = false)
    private String expectedTime;

    @Column(nullable = false)
    private Integer progressPercentage = 0;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String learningSequence;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public StudyPlan() {
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getLearningSequence() {
        return learningSequence;
    }

    public void setLearningSequence(String learningSequence) {
        this.learningSequence = learningSequence;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
