package com.afyasmart.backend.dto;

import lombok.Data;

@Data
public class PatientDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer age;
    private String gender;
    private String bloodGroup;
    private Double height;
    private Double weight;
    private String address;
    private String emergencyContact;
    private String emergencyPhone;
}