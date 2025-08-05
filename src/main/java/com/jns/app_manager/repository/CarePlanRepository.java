package com.jns.app_manager.repository;

import com.jns.app_manager.entity.CarePlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarePlanRepository extends JpaRepository<CarePlan, UUID> {
    Page<CarePlan> findAll(Specification<CarePlan> spec, Pageable pageable);
}