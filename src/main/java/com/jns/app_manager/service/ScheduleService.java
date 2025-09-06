package com.jns.app_manager.service;

import com.jns.app_manager.dtos.CarePlanResponseDTO;
import com.jns.app_manager.dtos.ScheduleRequestDTO;
import com.jns.app_manager.dtos.ScheduleResponseDTO;
import com.jns.app_manager.dtos.mapper.ScheduleMapper;
import com.jns.app_manager.entity.CarePlan;
import com.jns.app_manager.entity.Schedule;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.exceptions.ViolationIntegrityException;
import com.jns.app_manager.repository.CarePlanRepository;
import com.jns.app_manager.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CarePlanService carePlanService;
    private final ScheduleMapper scheduleMapper;
    private final ClientService clientService;

    public ScheduleResponseDTO findById(UUID id) {
        var schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Schedule not found"));
        return scheduleMapper.toResponse(schedule);
    }

    public ScheduleResponseDTO save(ScheduleRequestDTO dto) {
        var carePlan = carePlanService.findCarePlan(dto.carePlanId());

        var schedule = scheduleMapper.toEntity(dto, carePlan);
        validateScheduleConflict(dto, carePlan);
        var saved = scheduleRepository.save(schedule);

        return scheduleMapper.toResponse(saved);
    }

    public ScheduleResponseDTO update(UUID id, ScheduleRequestDTO dto) {
        var schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Schedule not found"));

        schedule.setDayOfWeek(dto.dayOfWeek());
        schedule.setSessionTime(dto.sessionTime());

        var updated = scheduleRepository.save(schedule);
        return scheduleMapper.toResponse(updated);
    }

    public void delete(UUID id) {
        var schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Schedule not found"));
        schedule.setCarePlan(null);
        scheduleRepository.save(schedule);
        scheduleRepository.delete(schedule);

    }

    private void validateScheduleConflict(ScheduleRequestDTO dto, CarePlan currentCarePlan) {
        Page<CarePlanResponseDTO> allCarePlansPage = carePlanService.findCarePlansByUserOrClientIdAndTitle(
                currentCarePlan.getUser().getId(), "", Pageable.unpaged()
        );

        allCarePlansPage.getContent().stream()
                .flatMap(plan -> plan.schedule().stream()
                        .filter(schedule -> schedule.getDayOfWeek().equalsIgnoreCase(dto.dayOfWeek()) &&
                                schedule.getSessionTime().equals(dto.sessionTime()))
                        .filter(schedule -> {
                            // Verifica se o schedule está dentro do intervalo de datas do plano
                            LocalDate start = plan.startDate();
                            LocalDate end = plan.expectedEndDate();
                            LocalDate currentStart = currentCarePlan.getStartDate();
                            LocalDate currentEnd = currentCarePlan.getExpectedEndDate();

                            // Se houver sobreposição entre os intervalos
                            boolean overlap = !end.isBefore(currentStart) && !start.isAfter(currentEnd);
                            return overlap;
                        })
                        .map(schedule -> new Object() {
                            final CarePlanResponseDTO planRef = plan;
                            final Schedule scheduleRef = schedule;
                        })
                )
                .findAny()
                .map(conflict -> {
                    var client = clientService.findClient(conflict.planRef.clientId());
                    String message = String.format(
                            "Você já possui uma agenda para o cliente: - %s - entre: %s e %s no dia:  %s , às: - %s -, no endereço: %s",
                            client.getName(),
                            conflict.planRef.startDate(),
                            conflict.planRef.expectedEndDate(),
                            conflict.scheduleRef.getDayOfWeek(),
                            conflict.scheduleRef.getSessionTime(),
                            client.getLocal()
                    );
                    throw new ViolationIntegrityException(message);
                });
    }
}