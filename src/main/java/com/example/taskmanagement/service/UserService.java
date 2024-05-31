package com.example.taskmanagement.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.taskmanagement.model.User;

public interface UserService {
    User saveUser(User user);


    User findByUsername(String username);


    UserDetails loadUserByUsername(String username);
}
