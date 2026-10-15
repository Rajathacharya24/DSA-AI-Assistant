package com.dsa.assistant.controller;

import com.dsa.assistant.dto.StudyPlanRequest;
import com.dsa.assistant.dto.StudyPlanResponse;
import com.dsa.assistant.security.CurrentUser;
import com.dsa.assistant.service.StudyPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    public StudyPlanController(StudyPlanService studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    @PostMapping
    public ResponseEntity<StudyPlanResponse> createPlan(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody StudyPlanRequest request) {
        if (currentUser == null) {
            throw new AccessDeniedException("Authentication required");
        }
        StudyPlanResponse response = studyPlanService.generatePlan(currentUser.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<StudyPlanResponse> getPlan(
            @PathVariable Long userId,
            @AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null || !userId.equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only access your own study plan.");
        }
        return ResponseEntity.ok(studyPlanService.getLatestPlan(userId));
    }
}
