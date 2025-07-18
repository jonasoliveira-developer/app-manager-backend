package com.jns.app_manager.domain;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class Schedule {
    private String dayOfWeek;
    private String sessionTime;
}