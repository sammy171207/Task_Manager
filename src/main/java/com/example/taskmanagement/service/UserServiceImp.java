package com.example.taskmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username);
        if (user != null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        String role=user.getRole();
        List<GrantedAuthority>authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);
    }


}
