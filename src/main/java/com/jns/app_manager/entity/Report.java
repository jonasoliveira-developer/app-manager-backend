package com.jns.app_manager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
@Entity(name = "reports")
public class Report extends Audit {

    @Id
    @GeneratedValue
    @Column(length = 36, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private String title;

    private String councilRegistrationNumber;

    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    private User user;

    @ManyToOne
    private Client client;

    @Lob
    private byte[] assignClient;

    private String assignClientMimeType;

    private String assignUrlClient;

    @Lob
    private byte[] assignUser;

    private String assignUserMimeType;

    private String assignUrlUser;
}