package com.dsa.assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeReviewResponse {
    private boolean correct;
    private String feedback;
    private String timeComplexity;
    private String spaceComplexity;
    private String suggestions;
}
