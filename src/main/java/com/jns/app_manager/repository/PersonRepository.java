package com.jns.app_manager.repository;

import com.jns.app_manager.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface PersonRepository<T extends Person> extends JpaRepository<T, Long> {
    Optional<T> findByEmail(String email); // login
    boolean existsById(UUID id);

}