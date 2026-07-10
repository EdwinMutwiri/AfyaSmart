package com.afyasmart.backend.dto;

import com.afyasmart.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private Boolean enabled;
}