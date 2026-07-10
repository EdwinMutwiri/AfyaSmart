package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.LoginRequest;
import com.afyasmart.backend.dto.RegisterRequest;
import com.afyasmart.backend.entity.Account;

public interface AuthService {

    Account register(RegisterRequest request);

    Account login(LoginRequest request);
}