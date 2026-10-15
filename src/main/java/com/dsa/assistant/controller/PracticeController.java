package com.dsa.assistant.controller;

import com.dsa.assistant.dto.PracticeSessionRequest;
import com.dsa.assistant.dto.PracticeSessionResponse;
import com.dsa.assistant.dto.PracticeValidationRequest;
import com.dsa.assistant.dto.PracticeValidationResponse;
import com.dsa.assistant.security.CurrentUser;
import com.dsa.assistant.service.PracticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/practice")
public class PracticeController {

    private final PracticeService practiceService;

    public PracticeController(PracticeService practiceService) {
        this.practiceService = practiceService;
    }

    @PostMapping("/start")
    public ResponseEntity<PracticeSessionResponse> startPractice(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody PracticeSessionRequest request) {
        if (currentUser == null) {
            throw new AccessDeniedException("Authentication required");
        }
        return ResponseEntity.ok(practiceService.startPracticeSession(currentUser.getId(), request));
    }

    @PostMapping("/validate")
    public ResponseEntity<PracticeValidationResponse> validatePractice(
            @RequestBody PracticeValidationRequest request) {
        return ResponseEntity.ok(practiceService.validatePractice(request));
    }
}
