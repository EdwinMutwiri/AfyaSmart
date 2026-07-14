package com.afyasmart.backend.repository;

import com.afyasmart.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.afyasmart.backend.entity.Role;
import java.util.List;


public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Account> findByRole(Role role);
}