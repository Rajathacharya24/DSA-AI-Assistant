package com.dsa.assistant.dto;

import java.time.LocalDateTime;

public class UserProgressDTO {
    private long problemsAttempted;
    private long problemsSolved;
    private long hintsUsed;
    private long topicsStudied;
    private long easySolved;
    private long mediumSolved;
    private long hardSolved;
    private int currentStreak;
    private LocalDateTime lastActivity;

    public UserProgressDTO() {
    }

    public UserProgressDTO(long problemsAttempted, long problemsSolved, long hintsUsed, long topicsStudied,
                           long easySolved, long mediumSolved, long hardSolved, int currentStreak, LocalDateTime lastActivity) {
        this.problemsAttempted = problemsAttempted;
        this.problemsSolved = problemsSolved;
        this.hintsUsed = hintsUsed;
        this.topicsStudied = topicsStudied;
        this.easySolved = easySolved;
        this.mediumSolved = mediumSolved;
        this.hardSolved = hardSolved;
        this.currentStreak = currentStreak;
        this.lastActivity = lastActivity;
    }

    public long getProblemsAttempted() {
        return problemsAttempted;
    }

    public void setProblemsAttempted(long problemsAttempted) {
        this.problemsAttempted = problemsAttempted;
    }

    public long getProblemsSolved() {
        return problemsSolved;
    }

    public void setProblemsSolved(long problemsSolved) {
        this.problemsSolved = problemsSolved;
    }

    public long getHintsUsed() {
        return hintsUsed;
    }

    public void setHintsUsed(long hintsUsed) {
        this.hintsUsed = hintsUsed;
    }

    public long getTopicsStudied() {
        return topicsStudied;
    }

    public void setTopicsStudied(long topicsStudied) {
        this.topicsStudied = topicsStudied;
    }

    public long getEasySolved() {
        return easySolved;
    }

    public void setEasySolved(long easySolved) {
        this.easySolved = easySolved;
    }

    public long getMediumSolved() {
        return mediumSolved;
    }

    public void setMediumSolved(long mediumSolved) {
        this.mediumSolved = mediumSolved;
    }

    public long getHardSolved() {
        return hardSolved;
    }

    public void setHardSolved(long hardSolved) {
        this.hardSolved = hardSolved;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }
}
