package com.jns.app_manager.repository;

import com.jns.app_manager.entity.Payment;
import com.jns.app_manager.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Page<Payment> findByClientIdAndTitleContainingIgnoreCase(UUID clientId, String title, Pageable pageable);
    List<Payment> findByPaymentStatus(PaymentStatus status);
    Optional<Payment> findByCarePlanId(UUID carePlanId);
}