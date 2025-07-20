package com.jns.app_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "reports")
public class Report extends Audit{
    @Id
    @Column(length = 36, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private String councilRegistrationNumber;
    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    private User user;

    @ManyToOne
    private Client client;

}