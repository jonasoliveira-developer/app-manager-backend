package com.jns.app_manager.entity;

import com.jns.app_manager.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@With
@Entity(name = "payments")
public class Payment extends Audit{
    @Id
    @GeneratedValue
    @Column(length = 36, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;


    private LocalDate openedDate;
    private LocalDate closedDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToOne
    private CarePlan carePlan;
}


