package com.dsa.assistant.service;

import com.dsa.assistant.model.Progress;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.User;
import com.dsa.assistant.model.enums.ProgressStatus;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.ProgressRepository;
import com.dsa.assistant.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;

    public ProgressService(ProgressRepository progressRepository, UserRepository userRepository, ProblemRepository problemRepository) {
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
    }

    public List<Progress> getProgressByUserId(Long userId) {
        return progressRepository.findByUserId(userId);
    }

    public void incrementHintsUsed(Long userId, Long problemId) {
        Optional<Progress> optionalProgress = progressRepository.findByUserIdAndProblemId(userId, problemId);
        
        Progress progress;
        if (optionalProgress.isPresent()) {
            progress = optionalProgress.get();
        } else {
            // Create a new progress record if it doesn't exist
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
            Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new IllegalArgumentException("Problem not found: " + problemId));
            
            progress = new Progress();
            progress.setUser(user);
            progress.setProblem(problem);
            progress.setStatus(ProgressStatus.IN_PROGRESS);
        }
        
        progress.setHintsUsed(progress.getHintsUsed() + 1);
        progressRepository.save(progress);
    }

    public void updateProgressStatus(Long userId, Long problemId, boolean isCorrect) {
        Optional<Progress> optionalProgress = progressRepository.findByUserIdAndProblemId(userId, problemId);
        
        Progress progress;
        if (optionalProgress.isPresent()) {
            progress = optionalProgress.get();
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
            Problem problem = problemRepository.findById(problemId)
                    .orElseThrow(() -> new IllegalArgumentException("Problem not found: " + problemId));
            
            progress = new Progress();
            progress.setUser(user);
            progress.setProblem(problem);
            progress.setHintsUsed(0);
        }
        
        progress.setStatus(isCorrect ? ProgressStatus.SOLVED : ProgressStatus.ATTEMPTED);
        progressRepository.save(progress);
    }
}
