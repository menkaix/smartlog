package com.menkaix.smartlog.dto;

import com.menkaix.smartlog.model.BugReport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BugReportRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Platform is required")
    private BugReport.Platform platform;

    @NotBlank(message = "App version is required")
    private String appVersion;

    @NotBlank(message = "OS version is required")
    private String osVersion;

    @NotBlank(message = "Device model is required")
    private String deviceModel;

    private BugReport.Severity severity;

    private String stackTrace;

    private String userEmail;

    private String stepsToReproduce;
}
