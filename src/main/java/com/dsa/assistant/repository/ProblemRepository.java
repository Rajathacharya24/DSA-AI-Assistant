package com.dsa.assistant.repository;

import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByTopicId(Long topicId);
    List<Problem> findByDifficulty(Difficulty difficulty);
    List<Problem> findByTopicNameIgnoreCaseAndDifficulty(String topicName, Difficulty difficulty);
}
