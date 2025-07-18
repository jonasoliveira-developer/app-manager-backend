package com.jns.app_manager.domain;

import com.jns.app_manager.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@With
@Entity
public class Payment {
    @Id
    @UuidGenerator
    private UUID id;


    private LocalDate openedDate;
    private LocalDate closedDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne
    private CarePlan carePlan;
}


