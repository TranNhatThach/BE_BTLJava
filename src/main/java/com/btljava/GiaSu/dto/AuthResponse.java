package com.btljava.GiaSu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Integer userId;
    private String username;
    private String email;
    private String message;
    private boolean success;
    private String token;
    private String role;
}
