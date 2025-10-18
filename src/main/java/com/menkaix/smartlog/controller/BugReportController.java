package com.menkaix.smartlog.controller;

import com.menkaix.smartlog.dto.BugReportRequest;
import com.menkaix.smartlog.dto.BugReportResponse;
import com.menkaix.smartlog.model.BugReport;
import com.menkaix.smartlog.service.BugReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bug-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class BugReportController {

    private final BugReportService bugReportService;

    /**
     * Create a new bug report
     * POST /api/bug-reports
     */
    @PostMapping
    public ResponseEntity<BugReportResponse> createBugReport(@Valid @RequestBody BugReportRequest request) {
        BugReportResponse response = bugReportService.createBugReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all bug reports
     * GET /api/bug-reports
     */
    @GetMapping
    public ResponseEntity<List<BugReportResponse>> getAllBugReports() {
        List<BugReportResponse> reports = bugReportService.getAllBugReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * Get a specific bug report by ID
     * GET /api/bug-reports/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BugReportResponse> getBugReportById(@PathVariable Long id) {
        BugReportResponse response = bugReportService.getBugReportById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get bug reports by platform (ANDROID, IOS, WEB)
     * GET /api/bug-reports/platform/{platform}
     */
    @GetMapping("/platform/{platform}")
    public ResponseEntity<List<BugReportResponse>> getBugReportsByPlatform(@PathVariable BugReport.Platform platform) {
        List<BugReportResponse> reports = bugReportService.getBugReportsByPlatform(platform);
        return ResponseEntity.ok(reports);
    }

    /**
     * Get bug reports by status (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
     * GET /api/bug-reports/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BugReportResponse>> getBugReportsByStatus(@PathVariable BugReport.Status status) {
        List<BugReportResponse> reports = bugReportService.getBugReportsByStatus(status);
        return ResponseEntity.ok(reports);
    }

    /**
     * Update the status of a bug report
     * PATCH /api/bug-reports/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<BugReportResponse> updateBugReportStatus(
            @PathVariable Long id,
            @RequestParam BugReport.Status status) {
        BugReportResponse response = bugReportService.updateBugReportStatus(id, status);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a bug report
     * DELETE /api/bug-reports/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBugReport(@PathVariable Long id) {
        bugReportService.deleteBugReport(id);
        return ResponseEntity.noContent().build();
    }
}
