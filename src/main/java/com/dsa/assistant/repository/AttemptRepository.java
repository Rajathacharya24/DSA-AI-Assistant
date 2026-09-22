package com.dsa.assistant.repository;

import com.dsa.assistant.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findByUserId(Long userId);
    List<Attempt> findByUserIdAndProblemId(Long userId, Long problemId);

    @Query("SELECT a.createdAt FROM Attempt a WHERE a.user.id = :userId ORDER BY a.createdAt DESC")
    List<LocalDateTime> findAttemptDatesByUserId(@Param("userId") Long userId);
}
