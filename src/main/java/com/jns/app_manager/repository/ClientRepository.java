package com.jns.app_manager.repository;

import com.jns.app_manager.entity.Client;
import com.jns.app_manager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Page<Client> findByUserIdAndNameContainingIgnoreCase(UUID userId, String name, Pageable pageable);
    Optional<Client>findByEmail(String email);
}