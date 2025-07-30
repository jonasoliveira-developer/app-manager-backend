package com.jns.app_manager.service;

import com.jns.app_manager.dtos.ScheduleRequestDTO;
import com.jns.app_manager.dtos.ScheduleResponseDTO;
import com.jns.app_manager.dtos.mapper.ScheduleMapper;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.CarePlanRepository;
import com.jns.app_manager.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CarePlanRepository carePlanRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleResponseDTO findById(UUID id) {
        var schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Schedule not found"));
        return scheduleMapper.toResponse(schedule);
    }

    public ScheduleResponseDTO save(ScheduleRequestDTO dto) {
        var carePlan = carePlanRepository.findById(dto.carePlanId())
                .orElseThrow(() -> new ObjectNotFoundException("Care Plan not found"));

        var schedule = scheduleMapper.toEntity(dto, carePlan);
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
        scheduleRepository.delete(schedule);
    }
}