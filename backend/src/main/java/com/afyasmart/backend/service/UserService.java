package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

}