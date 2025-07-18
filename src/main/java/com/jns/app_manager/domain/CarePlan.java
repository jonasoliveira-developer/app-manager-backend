package com.jns.app_manager.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@With
public class CarePlan {
    @Id
    @UuidGenerator
    private UUID id;


    @ManyToOne
    private User user;

    @ManyToOne
    private Client client;

    private List<Scheduled> schedule;

    @OneToOne(mappedBy = "carePlan")
    private Payment payment;

    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate actualEndDate;
}