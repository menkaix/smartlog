package com.menkaix.smartlog.repository;

import com.menkaix.smartlog.model.BugReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugReportRepository extends JpaRepository<BugReport, Long> {

    List<BugReport> findByPlatform(BugReport.Platform platform);

    List<BugReport> findByStatus(BugReport.Status status);

    List<BugReport> findBySeverity(BugReport.Severity severity);

    List<BugReport> findByPlatformAndStatus(BugReport.Platform platform, BugReport.Status status);
}
