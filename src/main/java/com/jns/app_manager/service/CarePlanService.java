package com.jns.app_manager.service;

import com.jns.app_manager.dtos.CarePlanRequestDTO;
import com.jns.app_manager.dtos.CarePlanResponseDTO;
import com.jns.app_manager.dtos.mapper.CarePlanMapper;
import com.jns.app_manager.entity.CarePlan;
import com.jns.app_manager.entity.User;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.CarePlanRepository;
import com.jns.app_manager.repository.ClientRepository;
import com.jns.app_manager.repository.UserRepository;
import com.jns.app_manager.spec.Specifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarePlanService {

    private final CarePlanRepository carePlanRepository;
    private final UserService userService;
    private final ClientService clientService;
    private final CarePlanMapper mapper;

    public CarePlanResponseDTO save(CarePlanRequestDTO dto) {

        var user = userService.findUser(dto.userId());
        var client = clientService.findClient(dto.clientId());

        var carePlan = CarePlan.builder()
                .title(dto.title())
                .user(user)
                .client(client)
                .startDate(dto.startDate())
                .expectedEndDate(dto.expectedEndDate())
                .schedule(dto.schedule())
                .build();

        return mapper.toResponse(carePlanRepository.save(carePlan));
    }


    public CarePlanResponseDTO findById(UUID id) {
        var carePlan = findCarePlan(id);

        return mapper.toResponse(carePlan);
    }

    public Page<CarePlanResponseDTO> findCarePlansByUserOrClientIdAndTitle(UUID id, String title, Pageable pageable) {
        Specification<CarePlan> spec = Specification.where(Specifications.byUserOrClientId(id));

        if (title != null && !title.isBlank()) {
            spec = spec.and(Specifications.titleContainsIgnoreCase(title));
        }
        return carePlanRepository.findAll(spec, pageable)
                .map(mapper::toResponse);
    }

    public void delete(UUID id) {
        var carePlan = findCarePlan(id);

        carePlanRepository.deleteById(carePlan.getId());
    }

    public CarePlan findCarePlan(UUID id) {
        return carePlanRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Plano de atendimento não encontrado: " + id));
    }
}