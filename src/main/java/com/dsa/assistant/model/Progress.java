package com.dsa.assistant.model;

import com.dsa.assistant.model.enums.ProgressStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "progress",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_progress_user_problem", columnNames = {"user_id", "problem_id"})
    }
)
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status;

    @Column(nullable = false)
    private Integer attempts = 0;

    @Column(nullable = false)
    private Integer hintsUsed = 0;

    private LocalDateTime solvedAt;

    public Progress() {
    }

    public Progress(Long id, User user, Problem problem, ProgressStatus status, Integer attempts, Integer hintsUsed, LocalDateTime solvedAt) {
        this.id = id;
        this.user = user;
        this.problem = problem;
        this.status = status;
        this.attempts = attempts != null ? attempts : 0;
        this.hintsUsed = hintsUsed != null ? hintsUsed : 0;
        this.solvedAt = solvedAt;
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

    public ProgressStatus getStatus() {
        return status;
    }

    public void setStatus(ProgressStatus status) {
        this.status = status;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Integer getHintsUsed() {
        return hintsUsed;
    }

    public void setHintsUsed(Integer hintsUsed) {
        this.hintsUsed = hintsUsed;
    }

    public LocalDateTime getSolvedAt() {
        return solvedAt;
    }

    public void setSolvedAt(LocalDateTime solvedAt) {
        this.solvedAt = solvedAt;
    }
}
