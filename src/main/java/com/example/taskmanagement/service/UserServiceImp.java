package com.example.taskmanagement.service;

import com.example.taskmanagement.config.JwtProvider;
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
 @Autowired
 private JwtProvider jwtProvider;

    @Override
    public User saveUser(User user)  throws Exception{
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

   @Override
   public User findByUserByJwtToken(String jwt) throws Exception{
        String username=jwtProvider.getUsernameFromJwtToken(jwt);
       System.out.println(username);
       User user=findByUsername(username);
       if(user==null){
           throw new Exception("User not found");
       }
        return user;
   }


}
