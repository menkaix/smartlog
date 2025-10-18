package com.menkaix.smartlog.service;

import com.menkaix.smartlog.dto.BugReportRequest;
import com.menkaix.smartlog.dto.BugReportResponse;
import com.menkaix.smartlog.exception.ResourceNotFoundException;
import com.menkaix.smartlog.model.BugReport;
import com.menkaix.smartlog.repository.BugReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BugReportService {

    private final BugReportRepository bugReportRepository;

    @Transactional
    public BugReportResponse createBugReport(BugReportRequest request) {
        log.info("Creating bug report: {} - Platform: {}", request.getTitle(), request.getPlatform());

        BugReport bugReport = BugReport.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .platform(request.getPlatform())
                .appVersion(request.getAppVersion())
                .osVersion(request.getOsVersion())
                .deviceModel(request.getDeviceModel())
                .severity(request.getSeverity() != null ? request.getSeverity() : BugReport.Severity.MEDIUM)
                .stackTrace(request.getStackTrace())
                .userEmail(request.getUserEmail())
                .stepsToReproduce(request.getStepsToReproduce())
                .build();

        BugReport savedBugReport = bugReportRepository.save(bugReport);
        log.info("Bug report created with ID: {}", savedBugReport.getId());

        return BugReportResponse.fromEntity(savedBugReport);
    }

    @Transactional(readOnly = true)
    public List<BugReportResponse> getAllBugReports() {
        return bugReportRepository.findAll()
                .stream()
                .map(BugReportResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BugReportResponse getBugReportById(Long id) {
        BugReport bugReport = bugReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bug report not found with id: " + id));
        return BugReportResponse.fromEntity(bugReport);
    }

    @Transactional(readOnly = true)
    public List<BugReportResponse> getBugReportsByPlatform(BugReport.Platform platform) {
        return bugReportRepository.findByPlatform(platform)
                .stream()
                .map(BugReportResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BugReportResponse> getBugReportsByStatus(BugReport.Status status) {
        return bugReportRepository.findByStatus(status)
                .stream()
                .map(BugReportResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BugReportResponse updateBugReportStatus(Long id, BugReport.Status status) {
        BugReport bugReport = bugReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bug report not found with id: " + id));

        bugReport.setStatus(status);
        BugReport updatedBugReport = bugReportRepository.save(bugReport);

        log.info("Bug report ID {} status updated to: {}", id, status);
        return BugReportResponse.fromEntity(updatedBugReport);
    }

    @Transactional
    public void deleteBugReport(Long id) {
        if (!bugReportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bug report not found with id: " + id);
        }
        bugReportRepository.deleteById(id);
        log.info("Bug report deleted with ID: {}", id);
    }
}
