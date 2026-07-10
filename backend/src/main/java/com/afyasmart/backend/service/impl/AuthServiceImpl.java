package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.AccountResponse;
import com.afyasmart.backend.dto.LoginRequest;
import com.afyasmart.backend.dto.RegisterRequest;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.exception.DuplicateResourceException;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                .role(request.getRole())
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
    public Account login(LoginRequest request) {
        throw new UnsupportedOperationException("Login will be implemented next.");
    }
}