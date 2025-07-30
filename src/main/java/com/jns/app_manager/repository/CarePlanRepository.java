package com.jns.app_manager.repository;

import com.jns.app_manager.entity.CarePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarePlanRepository extends JpaRepository<CarePlan, UUID> {
    List<CarePlan> findAllByUserId(UUID id);
    List<CarePlan> findAllByClientId(UUID id);
}