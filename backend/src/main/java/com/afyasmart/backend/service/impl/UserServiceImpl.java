package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.UserResponse;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;

    @Override
    public List<UserResponse> getAllUsers() {

        return accountRepository.findAll()
                .stream()
                .map(account -> UserResponse.builder()
                        .id(account.getId())
                        .firstName(account.getFirstName())
                        .lastName(account.getLastName())
                        .email(account.getEmail())
                        .role(account.getRole())
                        .enabled(account.getEnabled())
                        .build())
                .toList();
    }

}