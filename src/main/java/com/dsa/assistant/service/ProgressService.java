package com.dsa.assistant.service;

import com.dsa.assistant.dto.UserProgressDTO;
import com.dsa.assistant.model.Progress;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.User;
import com.dsa.assistant.model.enums.ProgressStatus;
import com.dsa.assistant.repository.AttemptRepository;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.ProgressRepository;
import com.dsa.assistant.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final AttemptRepository attemptRepository;

    public ProgressService(ProgressRepository progressRepository, UserRepository userRepository, ProblemRepository problemRepository, AttemptRepository attemptRepository) {
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.attemptRepository = attemptRepository;
    }

    public UserProgressDTO getUserProgress(Long userId) {
        // Validate user exists
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        long problemsAttempted = progressRepository.countProblemsAttempted(userId);
        long problemsSolved = progressRepository.countProblemsSolved(userId);
        long hintsUsed = progressRepository.sumHintsUsed(userId);
        long topicsStudied = progressRepository.countTopicsStudied(userId);
        long easySolved = progressRepository.countEasySolved(userId);
        long mediumSolved = progressRepository.countMediumSolved(userId);
        long hardSolved = progressRepository.countHardSolved(userId);

        List<LocalDateTime> attemptDates = attemptRepository.findAttemptDatesByUserId(userId);

        LocalDateTime lastActivity = attemptDates.isEmpty() ? null : attemptDates.get(0);
        int currentStreak = calculateStreak(attemptDates);

        return new UserProgressDTO(
                problemsAttempted,
                problemsSolved,
                hintsUsed,
                topicsStudied,
                easySolved,
                mediumSolved,
                hardSolved,
                currentStreak,
                lastActivity
        );
    }

    private int calculateStreak(List<LocalDateTime> attemptDates) {
        if (attemptDates.isEmpty()) return 0;

        List<LocalDate> distinctDates = attemptDates.stream()
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        LocalDate today = LocalDate.now();
        LocalDate checkDate = distinctDates.get(0);

        // Streak is broken if the last activity was before yesterday
        if (checkDate.isBefore(today.minusDays(1))) {
            return 0;
        }

        int streak = 0;
        LocalDate current = checkDate;

        for (LocalDate date : distinctDates) {
            if (date.equals(current)) {
                streak++;
                current = current.minusDays(1);
            } else {
                break;
            }
        }

        return streak;
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
