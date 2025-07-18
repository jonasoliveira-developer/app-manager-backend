package com.jns.app_manager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Report {
    @Id
    @UuidGenerator
    private UUID id;


    @ManyToOne
    private User user;

    @ManyToOne
    private Client client;

    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String text;
}