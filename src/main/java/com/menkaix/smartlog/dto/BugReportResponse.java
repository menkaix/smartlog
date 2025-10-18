package com.menkaix.smartlog.dto;

import com.menkaix.smartlog.model.BugReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BugReportResponse {

    private Long id;
    private String title;
    private String description;
    private BugReport.Platform platform;
    private String appVersion;
    private String osVersion;
    private String deviceModel;
    private BugReport.Severity severity;
    private BugReport.Status status;
    private String stackTrace;
    private String userEmail;
    private String stepsToReproduce;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BugReportResponse fromEntity(BugReport bugReport) {
        return BugReportResponse.builder()
                .id(bugReport.getId())
                .title(bugReport.getTitle())
                .description(bugReport.getDescription())
                .platform(bugReport.getPlatform())
                .appVersion(bugReport.getAppVersion())
                .osVersion(bugReport.getOsVersion())
                .deviceModel(bugReport.getDeviceModel())
                .severity(bugReport.getSeverity())
                .status(bugReport.getStatus())
                .stackTrace(bugReport.getStackTrace())
                .userEmail(bugReport.getUserEmail())
                .stepsToReproduce(bugReport.getStepsToReproduce())
                .createdAt(bugReport.getCreatedAt())
                .updatedAt(bugReport.getUpdatedAt())
                .build();
    }
}
