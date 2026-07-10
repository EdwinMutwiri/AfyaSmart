package com.afyasmart.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Welcome to AfyaSmart API";
    }

    @GetMapping("/health")
    public String health() {
        return "AfyaSmart Backend is running successfully!";
    }
}