package com.dsa.assistant.controller;

import com.dsa.assistant.dto.CreateProblemDTO;
import com.dsa.assistant.dto.CodeSubmitRequest;
import com.dsa.assistant.dto.ProblemDTO;
import com.dsa.assistant.security.CurrentUser;
import com.dsa.assistant.service.ProblemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;
    private final com.dsa.assistant.service.CodeReviewService codeReviewService;

    public ProblemController(ProblemService problemService, com.dsa.assistant.service.CodeReviewService codeReviewService) {
        this.problemService = problemService;
        this.codeReviewService = codeReviewService;
    }

    @GetMapping
    public ResponseEntity<List<ProblemDTO>> getAllProblems() {
        return ResponseEntity.ok(problemService.getAllProblems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDTO> getProblemById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemById(id));
    }

    @GetMapping("/topic/{topic}")
    public ResponseEntity<List<ProblemDTO>> getProblemsByTopic(@PathVariable String topic) {
        return ResponseEntity.ok(problemService.getProblemsByTopic(topic));
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<ProblemDTO>> getProblemsByDifficulty(@PathVariable String difficulty) {
        return ResponseEntity.ok(problemService.getProblemsByDifficulty(difficulty));
    }

    @PostMapping
    public ResponseEntity<ProblemDTO> createProblem(@Valid @RequestBody CreateProblemDTO createProblemDTO) {
        ProblemDTO createdProblem = problemService.createProblem(createProblemDTO);
        return new ResponseEntity<>(createdProblem, HttpStatus.CREATED);
    }

    @PostMapping("/{problemId}/submit")
    public ResponseEntity<com.dsa.assistant.dto.CodeReviewResponse> submitCode(
            @PathVariable Long problemId,
            @AuthenticationPrincipal CurrentUser currentUser,
            @Valid @RequestBody CodeSubmitRequest request) {
        com.dsa.assistant.dto.CodeReviewResponse response = codeReviewService.reviewAndSubmitCode(problemId, currentUser.getId(), request);
        return ResponseEntity.ok(response);
    }
}
