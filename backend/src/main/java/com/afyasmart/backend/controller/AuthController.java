package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.dto.AccountResponse;
import com.afyasmart.backend.dto.RegisterRequest;
import com.afyasmart.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccountResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AccountResponse account = authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.<AccountResponse>builder()
                        .success(true)
                        .message("Account created successfully")
                        .data(account)
                        .build()
        );
    }
}