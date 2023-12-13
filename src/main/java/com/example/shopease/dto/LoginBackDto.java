package com.example.shopease.dto;

import com.example.shopease.enums.Role;
import lombok.Data;

@Data
public class LoginBackDto {
    private int id;

    private Role role;

    private String token;
}
