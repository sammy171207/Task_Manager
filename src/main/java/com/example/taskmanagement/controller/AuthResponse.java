package com.example.taskmanagement.controller;

import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String message;
}
