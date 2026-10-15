package com.dsa.assistant.repository;

import com.dsa.assistant.model.PracticeSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeSessionRepository extends JpaRepository<PracticeSession, Long> {
    List<PracticeSession> findByUserIdOrderByCreatedAtDesc(Long userId);
}
