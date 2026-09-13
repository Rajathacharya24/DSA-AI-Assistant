package com.dsa.assistant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getHealthStatus() {
        Map<String, Object> response = Map.of(
                "status", "UP",
                "application", "AI DSA Study Assistant Agent",
                "version", "1.0.0-BASE",
                "timestamp", LocalDateTime.now().toString(),
                "message", "Base Project is up and running successfully!"
        );
        return ResponseEntity.ok(response);
    }
}
