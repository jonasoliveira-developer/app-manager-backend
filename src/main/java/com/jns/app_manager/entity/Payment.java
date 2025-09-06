package com.jns.app_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jns.app_manager.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
@Entity(name = "payments")
public class Payment extends Audit{
    @Id
    @GeneratedValue
    @Column(length = 36, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private BigDecimal amount;

    private LocalDate openedDate;
    private LocalDate closedDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JsonIgnore
    private Client client;
}


