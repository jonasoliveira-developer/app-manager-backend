package com.jns.app_manager.entity;

import com.jns.app_manager.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@With
@Entity(name = "clients")
public class Client {
    @Id
    @Column(length = 36, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;


    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String age;
    private String weight;
    private String height;
    private String local;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private String ImageProfile;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "client")
    private List<CarePlan> carePlans;
}