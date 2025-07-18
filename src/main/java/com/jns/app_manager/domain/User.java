package com.jns.app_manager.domain;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@With
@Entity
public class User {
    @Id
    @UuidGenerator
    private UUID id;


    private String name;
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Client> clients;

    @OneToMany(mappedBy = "user")
    private List<CarePlan> carePlans;
}