package com.dsa.assistant.dto;

import java.util.List;

public class PracticeValidationResponse {
    private String bestCase;
    private String worstCase;
    private String reasoning;
    private List<String> checklist;
    private Integer score;

    public String getBestCase() {
        return bestCase;
    }

    public void setBestCase(String bestCase) {
        this.bestCase = bestCase;
    }

    public String getWorstCase() {
        return worstCase;
    }

    public void setWorstCase(String worstCase) {
        this.worstCase = worstCase;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public List<String> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<String> checklist) {
        this.checklist = checklist;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
