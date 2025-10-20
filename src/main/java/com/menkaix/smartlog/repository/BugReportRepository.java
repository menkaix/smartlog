package com.menkaix.smartlog.repository;

import com.menkaix.smartlog.model.BugReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugReportRepository extends JpaRepository<BugReport, Long> {

    Page<BugReport> findByPlatform(BugReport.Platform platform, Pageable pageable);

    Page<BugReport> findByStatus(BugReport.Status status, Pageable pageable);

    Page<BugReport> findBySeverity(BugReport.Severity severity, Pageable pageable);

    Page<BugReport> findByPlatformAndStatus(BugReport.Platform platform, BugReport.Status status, Pageable pageable);

    // Kept for backward compatibility or specific non-paginated use cases
    List<BugReport> findByPlatform(BugReport.Platform platform);

    List<BugReport> findByStatus(BugReport.Status status);

    List<BugReport> findBySeverity(BugReport.Severity severity);

    List<BugReport> findByPlatformAndStatus(BugReport.Platform platform, BugReport.Status status);
}
