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

    public CodeReviewResponse() {
    }

    public CodeReviewResponse(boolean correct, String feedback, String timeComplexity, String spaceComplexity, String suggestions) {
        this.correct = correct;
        this.feedback = feedback;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
        this.suggestions = suggestions;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getTimeComplexity() {
        return timeComplexity;
    }

    public void setTimeComplexity(String timeComplexity) {
        this.timeComplexity = timeComplexity;
    }

    public String getSpaceComplexity() {
        return spaceComplexity;
    }

    public void setSpaceComplexity(String spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }
}
