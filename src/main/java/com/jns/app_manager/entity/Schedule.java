package com.jns.app_manager.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@With
@Entity(name = "schedules")
public class Schedule extends Audit{

    @Id
    @GeneratedValue
    @Column(length = 36, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private String dayOfWeek;
    private String sessionTime;

    @ManyToOne
    private CarePlan carePlan;

}