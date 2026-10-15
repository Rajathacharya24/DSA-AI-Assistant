package com.dsa.assistant.dto;

public class PracticeValidationRequest {
    private String approach;
    private String constraints;
    private String edgeCases;
    private String complexity;

    public String getApproach() {
        return approach;
    }

    public void setApproach(String approach) {
        this.approach = approach;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public String getEdgeCases() {
        return edgeCases;
    }

    public void setEdgeCases(String edgeCases) {
        this.edgeCases = edgeCases;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }
}
