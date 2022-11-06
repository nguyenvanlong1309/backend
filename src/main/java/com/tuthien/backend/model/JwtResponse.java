package com.tuthien.backend.model;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private UserResponse user;
}
