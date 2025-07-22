package com.jns.app_manager.entity;

import com.jns.app_manager.enums.AccessLevel;
import com.jns.app_manager.enums.AccountStatus;
import com.jns.app_manager.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
public class User extends Audit {
    @Id
    @GeneratedValue
    @Column(length = 36, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String councilRegistrationNumber;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel = AccessLevel.USER;

    private String imageProfile;

    @OneToMany(mappedBy = "user")
    private List<Client> clients;

    @OneToMany(mappedBy = "user")
    private List<CarePlan> carePlans;

    @OneToMany(mappedBy = "user")
    private List<Report> reports;
}