package com.jns.app_manager.entity;

import com.jns.app_manager.enums.AccessLevel;
import com.jns.app_manager.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "clients")
@Entity
public class Client extends Person{
    private String age;
    private String weight;
    private String height;
    private String local;


    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "client")
    private List<CarePlan> carePlans;

    @OneToMany(mappedBy = "client")
    private List<Payment> payments;
}