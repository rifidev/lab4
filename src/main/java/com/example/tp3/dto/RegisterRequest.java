package com.example.tp3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String role; // USER ou ADMIN (pour le lab, ADMIN sera limité)
}