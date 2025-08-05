package com.jns.app_manager.repository;

import com.jns.app_manager.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    Page<Report> findByClientIdAndTitleContainingIgnoreCase(UUID clientId, String title, Pageable pageable);
}