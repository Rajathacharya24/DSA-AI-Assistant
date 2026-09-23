package com.dsa.assistant.dto;

public class RecommendationResponse {
    private String recommendedTopic;
    private Long nextProblemId;
    private String nextProblemTitle;
    private String recommendedDifficulty;
    private String reasoning;

    public RecommendationResponse() {
    }

    public RecommendationResponse(String recommendedTopic, Long nextProblemId, String nextProblemTitle, String recommendedDifficulty, String reasoning) {
        this.recommendedTopic = recommendedTopic;
        this.nextProblemId = nextProblemId;
        this.nextProblemTitle = nextProblemTitle;
        this.recommendedDifficulty = recommendedDifficulty;
        this.reasoning = reasoning;
    }

    public String getRecommendedTopic() {
        return recommendedTopic;
    }

    public void setRecommendedTopic(String recommendedTopic) {
        this.recommendedTopic = recommendedTopic;
    }

    public Long getNextProblemId() {
        return nextProblemId;
    }

    public void setNextProblemId(Long nextProblemId) {
        this.nextProblemId = nextProblemId;
    }

    public String getNextProblemTitle() {
        return nextProblemTitle;
    }

    public void setNextProblemTitle(String nextProblemTitle) {
        this.nextProblemTitle = nextProblemTitle;
    }

    public String getRecommendedDifficulty() {
        return recommendedDifficulty;
    }

    public void setRecommendedDifficulty(String recommendedDifficulty) {
        this.recommendedDifficulty = recommendedDifficulty;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}
