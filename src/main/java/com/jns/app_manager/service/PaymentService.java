package com.jns.app_manager.service;

import com.jns.app_manager.dtos.PaymentRequestDTO;
import com.jns.app_manager.dtos.PaymentResponseDTO;
import com.jns.app_manager.dtos.mapper.PaymentMapper;
import com.jns.app_manager.enums.PaymentStatus;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.CarePlanRepository;
import com.jns.app_manager.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CarePlanRepository carePlanRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResponseDTO findById(UUID id) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Payment not found"));
        return paymentMapper.toResponse(payment);
    }

    public List<PaymentResponseDTO> findAll() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponseDTO save(PaymentRequestDTO dto) {
        var carePlan = carePlanRepository.findById(dto.carePlanId())
                .orElseThrow(() -> new ObjectNotFoundException("Care Plan not found"));

        var payment = paymentMapper.toEntity(dto, carePlan);
        var saved = paymentRepository.save(payment);

        return paymentMapper.toResponse(saved);
    }

    public PaymentResponseDTO update(UUID id, PaymentRequestDTO dto) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Payment not found"));

        payment.setPaymentStatus(dto.paymentStatus());

        if(dto.paymentStatus().equals(PaymentStatus.CLOSED)) {
            payment.setClosedDate(LocalDate.now());
        }

        var updated = paymentRepository.save(payment);
        return paymentMapper.toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Payment not found"));
        paymentRepository.deleteById(payment.getId());
    }
}