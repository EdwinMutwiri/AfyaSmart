package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.AccountResponse;
import com.afyasmart.backend.dto.LoginRequest;
import com.afyasmart.backend.dto.LoginResponse;
import com.afyasmart.backend.dto.RegisterRequest;

public interface AuthService {

    AccountResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

}