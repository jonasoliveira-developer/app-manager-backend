package com.jns.app_manager.repository;

import com.jns.app_manager.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findAllByUserId(UUID userId);
    List<Report> findAllByClientId(UUID clientId);
}