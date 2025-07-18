package com.jns.app_manager.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Client {
    @Id
    @UuidGenerator
    private UUID id;


    private String name;
    private String email;
    private String password;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "client")
    private List<CarePlan> carePlans;
}