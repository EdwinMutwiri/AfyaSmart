package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.dto.RegisterRequest;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Account>> register(
            @Valid @RequestBody RegisterRequest request) {

        Account account = authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.<Account>builder()
                        .success(true)
                        .message("Account created successfully")
                        .data(account)
                        .build()
        );
    }
}