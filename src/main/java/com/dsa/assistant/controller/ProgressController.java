package com.dsa.assistant.controller;

import com.dsa.assistant.dto.UserProgressDTO;
import com.dsa.assistant.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/{userId}/progress")
    public ResponseEntity<UserProgressDTO> getUserProgress(@PathVariable Long userId) {
        UserProgressDTO progress = progressService.getUserProgress(userId);
        return ResponseEntity.ok(progress);
    }
}
