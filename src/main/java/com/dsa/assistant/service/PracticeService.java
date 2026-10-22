package com.dsa.assistant.service;

import com.dsa.assistant.dto.PracticeSessionRequest;
import com.dsa.assistant.dto.PracticeSessionResponse;
import com.dsa.assistant.dto.PracticeValidationRequest;
import com.dsa.assistant.dto.PracticeValidationResponse;
import com.dsa.assistant.model.PracticeSession;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.User;
import com.dsa.assistant.repository.PracticeSessionRepository;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PracticeService {

    private final PracticeSessionRepository practiceSessionRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    public PracticeService(PracticeSessionRepository practiceSessionRepository,
                           ProblemRepository problemRepository,
                           UserRepository userRepository) {
        this.practiceSessionRepository = practiceSessionRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }

    public PracticeSessionResponse startPracticeSession(Long userId, PracticeSessionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with id: " + request.getProblemId()));

        String reasoningPrompt = "Think before coding: identify the problem, constraints, input/output, and the best algorithm before writing the final solution.";
        List<String> checklist = Arrays.asList(
                "Read the statement carefully",
                "Understand the input and output",
                "Find the constraints and edge cases",
                "Pick the right data structure or algorithm",
                "Estimate time and space complexity"
        );

        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setProblem(problem);
        session.setReasoningPrompt(reasoningPrompt);
        session.setChecklist(String.join(" | ", checklist));

        PracticeSession savedSession = practiceSessionRepository.save(session);
        return mapToResponse(savedSession, checklist);
    }

    public PracticeValidationResponse validatePractice(PracticeValidationRequest request) {
        List<String> checklist = Arrays.asList(
                "Approach is clearly explained",
                "Constraints are understood",
                "Edge cases are considered",
                "Complexity is estimated",
                "Solution is simple and correct"
        );

        PracticeValidationResponse response = new PracticeValidationResponse();
        response.setBestCase("best case: the algorithm behaves efficiently when inputs are already favorable or minimal.");
        response.setWorstCase("worst case: the algorithm should still satisfy the constraints even with the largest input size.");
        response.setReasoning("Before submission, verify the approach, edge cases, and complexity to ensure the logic is valid and robust.");
        response.setChecklist(checklist);
        response.setScore(8);

        if (request != null) {
            if (request.getApproach() != null && !request.getApproach().isBlank()) {
                response.setReasoning("Your approach is being evaluated: " + request.getApproach() + ". Check whether it matches the constraints and data size.");
            }
            if (request.getComplexity() != null && !request.getComplexity().isBlank()) {
                response.setReasoning(response.getReasoning() + " Complexity note: " + request.getComplexity());
            }
        }

        return response;
    }

    private PracticeSessionResponse mapToResponse(PracticeSession session, List<String> checklist) {
        PracticeSessionResponse response = new PracticeSessionResponse();
        response.setId(session.getId());
        response.setUserId(session.getUser() != null ? session.getUser().getId() : null);
        response.setProblemId(session.getProblem() != null ? session.getProblem().getId() : null);
        response.setProblemTitle(session.getProblem() != null ? session.getProblem().getTitle() : null);
        response.setDifficulty(session.getProblem() != null && session.getProblem().getDifficulty() != null
                ? session.getProblem().getDifficulty().name() : null);
        response.setProblemDescription(session.getProblem() != null ? session.getProblem().getDescription() : null);
        response.setReasoningPrompt(session.getReasoningPrompt());
        response.setChecklist(checklist);
        response.setSampleInput(session.getProblem() != null ? session.getProblem().getExampleInput() : null);
        response.setSampleOutput(session.getProblem() != null ? session.getProblem().getExampleOutput() : null);
        response.setCreatedAt(session.getCreatedAt());
        return response;
    }
}
