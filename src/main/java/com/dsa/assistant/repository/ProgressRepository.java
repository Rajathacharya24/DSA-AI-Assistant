package com.dsa.assistant.repository;

import com.dsa.assistant.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByUserId(Long userId);
    Optional<Progress> findByUserIdAndProblemId(Long userId, Long problemId);

    @Query("SELECT COUNT(p) FROM Progress p WHERE p.user.id = :userId")
    long countProblemsAttempted(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Progress p WHERE p.user.id = :userId AND p.status = 'SOLVED'")
    long countProblemsSolved(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(p.hintsUsed), 0) FROM Progress p WHERE p.user.id = :userId")
    long sumHintsUsed(@Param("userId") Long userId);

    @Query("SELECT COUNT(DISTINCT p.problem.topic.id) FROM Progress p WHERE p.user.id = :userId")
    long countTopicsStudied(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Progress p WHERE p.user.id = :userId AND p.status = 'SOLVED' AND p.problem.difficulty = 'EASY'")
    long countEasySolved(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Progress p WHERE p.user.id = :userId AND p.status = 'SOLVED' AND p.problem.difficulty = 'MEDIUM'")
    long countMediumSolved(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Progress p WHERE p.user.id = :userId AND p.status = 'SOLVED' AND p.problem.difficulty = 'HARD'")
    long countHardSolved(@Param("userId") Long userId);
}
