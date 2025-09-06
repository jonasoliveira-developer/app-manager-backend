package com.jns.app_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedules")
@Entity
public class Schedule extends Audit {

    @Id
    @GeneratedValue
    @Column(length = 36, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private String dayOfWeek;
    private String sessionTime;

    private String color;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "care_plan_id", nullable = true)
    private CarePlan carePlan;

}