package com.jns.app_manager.service;

import com.jns.app_manager.dtos.PaymentRequestDTO;
import com.jns.app_manager.dtos.PaymentResponseDTO;
import com.jns.app_manager.dtos.mapper.PaymentMapper;
import com.jns.app_manager.entity.Payment;
import com.jns.app_manager.entity.User;
import com.jns.app_manager.enums.PaymentStatus;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.CarePlanRepository;
import com.jns.app_manager.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CarePlanService carePlanService;
    private final ClientService clientService;
    private final PaymentMapper paymentMapper;

    public PaymentResponseDTO findById(UUID id) {
        var payment = findPayment(id);
        return paymentMapper.toResponse(payment);
    }

    public Page<PaymentResponseDTO> findAllByClientIdAndTitle(UUID clientId, String title, Pageable pageable) {
        return paymentRepository.findByClientIdAndTitleContainingIgnoreCase(clientId, title, pageable)
                .map(paymentMapper::toResponse);
    }

    public PaymentResponseDTO save(PaymentRequestDTO dto) {
        var carePlan = carePlanService.findCarePlan(dto.carePlanId());
        var client = clientService.findClient(dto.clientId());
        var payment = paymentMapper.toEntity(dto, carePlan, client);
        var saved = paymentRepository.save(payment);
        return paymentMapper.toResponse(saved);
    }

    public PaymentResponseDTO update(UUID id, PaymentRequestDTO dto) {
        var payment = findPayment(id);
            payment.setTitle(dto.title());
            payment.setPaymentStatus(dto.paymentStatus());
        if(dto.paymentStatus().equals(PaymentStatus.CLOSED)) {
            payment.setClosedDate(LocalDate.now());
        }
        var updated = paymentRepository.save(payment);
        return paymentMapper.toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        var payment = findPayment(id);
        paymentRepository.deleteById(payment.getId());
    }

    public Payment findPayment(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pagamento não encontrado: " + id));
    }
}