package com.dsa.assistant.controller;

import com.dsa.assistant.dto.CreateProblemDTO;
import com.dsa.assistant.dto.ProblemDTO;
import com.dsa.assistant.service.ProblemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
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
}
