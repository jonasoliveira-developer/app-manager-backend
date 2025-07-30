package com.jns.app_manager.service;

import com.jns.app_manager.dtos.CarePlanRequestDTO;
import com.jns.app_manager.dtos.CarePlanResponseDTO;
import com.jns.app_manager.dtos.mapper.CarePlanMapper;
import com.jns.app_manager.entity.CarePlan;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.CarePlanRepository;
import com.jns.app_manager.repository.ClientRepository;
import com.jns.app_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarePlanService {

    private final CarePlanRepository carePlanRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final CarePlanMapper mapper;

    public CarePlanResponseDTO save(CarePlanRequestDTO dto) {

        var user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        var client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new ObjectNotFoundException("Client not found"));

        var carePlan = CarePlan.builder()
                .user(user)
                .client(client)
                .payment(null)
                .startDate(dto.startDate())
                .expectedEndDate(dto.expectedEndDate())
                .schedule(dto.schedule())
                .build();

        return mapper.toResponse(carePlanRepository.save(carePlan));
    }


    public CarePlanResponseDTO findById(UUID id) {
        var carePlan = carePlanRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Care Plan not found"));

        return mapper.toResponse(carePlan);
    }

    public List<CarePlanResponseDTO> findAllCarePlansByUserOrClientId(UUID id) {
        var plans = userRepository.existsById(id)
                ? carePlanRepository.findAllByUserId(id)
                : clientRepository.existsById(id)
                ? carePlanRepository.findAllByClientId(id)
                : throwNotFound();

        return plans.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void delete(UUID id) {
        var carePlan = carePlanRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Care Plan not found"));

        carePlanRepository.deleteById(carePlan.getId());
    }

    private List<CarePlan> throwNotFound() {
        throw new ObjectNotFoundException("ID não corresponde a nenhum User ou Client");
    }
}