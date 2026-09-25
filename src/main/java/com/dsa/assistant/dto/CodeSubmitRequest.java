package com.dsa.assistant.dto;

import jakarta.validation.constraints.NotBlank;

public class CodeSubmitRequest {

    @NotBlank(message = "Code is required")
    private String code;

    public CodeSubmitRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
