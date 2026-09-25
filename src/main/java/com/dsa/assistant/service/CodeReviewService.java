package com.dsa.assistant.service;

import com.dsa.assistant.dto.CodeReviewResponse;
import com.dsa.assistant.dto.CodeSubmitRequest;
import com.dsa.assistant.model.Attempt;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.User;
import com.dsa.assistant.model.enums.AttemptResult;
import com.dsa.assistant.repository.AttemptRepository;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CodeReviewService {

    private final AgentService agentService;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final AttemptRepository attemptRepository;
    private final ProgressService progressService;

    public CodeReviewService(AgentService agentService, ProblemRepository problemRepository,
                             UserRepository userRepository, AttemptRepository attemptRepository,
                             ProgressService progressService) {
        this.agentService = agentService;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
        this.progressService = progressService;
    }

    @Transactional
        public CodeReviewResponse reviewAndSubmitCode(Long problemId, Long userId, CodeSubmitRequest request) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with id: " + problemId));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Call the AI Agent to review the code
        CodeReviewResponse reviewResponse = agentService.reviewCode(problem.getTitle(), problem.getDescription(), request.getCode());

        // Create an attempt record
        Attempt attempt = new Attempt();
        attempt.setUser(user);
        attempt.setProblem(problem);
        attempt.setSubmittedCode(request.getCode());
        attempt.setResult(reviewResponse.isCorrect() ? AttemptResult.ACCEPTED : AttemptResult.WRONG_ANSWER);
        attemptRepository.save(attempt);

        // Update progress status
        progressService.updateProgressStatus(user.getId(), problem.getId(), reviewResponse.isCorrect());

        return reviewResponse;
    }
}
