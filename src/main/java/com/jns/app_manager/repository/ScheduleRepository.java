package com.jns.app_manager.repository;

import com.jns.app_manager.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findAllByCarePlan_UserId(UUID userId);
    List<Schedule> findAllByCarePlan_ClientId(UUID clientId);
}
