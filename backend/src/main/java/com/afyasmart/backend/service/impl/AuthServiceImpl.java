package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.AccountResponse;
import com.afyasmart.backend.dto.LoginRequest;
import com.afyasmart.backend.dto.LoginResponse;
import com.afyasmart.backend.dto.RegisterRequest;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.exception.DuplicateResourceException;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.afyasmart.backend.entity.Role;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AccountResponse register(RegisterRequest request) {

        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered.");
        }

        Account account = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .enabled(true)
                .build();

        Account savedAccount = accountRepository.save(account);

        return AccountResponse.builder()
                .id(savedAccount.getId())
                .firstName(savedAccount.getFirstName())
                .lastName(savedAccount.getLastName())
                .email(savedAccount.getEmail())
                .role(savedAccount.getRole())
                .enabled(savedAccount.getEnabled())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return LoginResponse.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .role(account.getRole())
                .enabled(account.getEnabled())
                .message("Login successful")
                .build();
    }
}