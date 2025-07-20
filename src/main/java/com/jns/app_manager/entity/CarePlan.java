package com.jns.app_manager.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@With
@Entity(name = "care_plans")
public class CarePlan extends Audit{
    @Id
    @Column(length = 36, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "carePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedule;

    @OneToOne(mappedBy = "carePlan")
    private Payment payment;

    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate actualEndDate;
}