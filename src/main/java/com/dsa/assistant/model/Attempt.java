package com.dsa.assistant.model;

import com.dsa.assistant.model.enums.AttemptResult;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attempts")
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String submittedCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttemptResult result;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Attempt() {
    }

    public Attempt(Long id, User user, Problem problem, String submittedCode, AttemptResult result, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.problem = problem;
        this.submittedCode = submittedCode;
        this.result = result;
        this.createdAt = createdAt;
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

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public String getSubmittedCode() {
        return submittedCode;
    }

    public void setSubmittedCode(String submittedCode) {
        this.submittedCode = submittedCode;
    }

    public AttemptResult getResult() {
        return result;
    }

    public void setResult(AttemptResult result) {
        this.result = result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
