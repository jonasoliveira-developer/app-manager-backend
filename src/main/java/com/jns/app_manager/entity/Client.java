package com.jns.app_manager.entity;

import com.jns.app_manager.enums.AccessLevel;
import com.jns.app_manager.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@With
@Entity(name = "clients")
public class Client extends Audit{
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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel = AccessLevel.CLIENT;

    private String ImageProfile;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "client")
    private List<CarePlan> carePlans;
}