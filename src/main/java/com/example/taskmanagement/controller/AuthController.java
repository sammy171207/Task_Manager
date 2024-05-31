package com.example.taskmanagement.controller;
import com.example.taskmanagement.config.JwtProvider;
import com.example.taskmanagement.model.User;


import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.request.LoginRequest;
import com.example.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtProvider jwtProvider;
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user)throws Exception {

//        Optional<User> isUserName=userRepository.findByUsername(user.getUsername());
//        if(isUserName==null){
//            throw  new Exception("user is reg alredy");
//        }

        User createUser=new User();
        createUser.setUsername(user.getUsername());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
         User savedUser=userRepository.save(createUser);

         Authentication authentication=new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=jwtProvider.generateToken(authentication);
        AuthResponse authResponse=new AuthResponse();
        authResponse.setToken(jwt);
        authResponse.setMessage("reg success");
        authResponse.setUsername(user.getUsername());
        return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public  ResponseEntity<AuthResponse>loginin(@RequestBody LoginRequest request){

       String  username= request.getUsername();
       String password= request.getPassword();
       Authentication authentication=authenticate(username,password);
        Collection<?extends GrantedAuthority>authorities=authentication.getAuthorities();
        String role=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
        String jwt=jwtProvider.generateToken(authentication);
        AuthResponse authResponse=new AuthResponse();
        authResponse.setToken(jwt);
        authResponse.setMessage("login success");
        authResponse.setRole(role);
        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails=userService.loadUserByUsername(username);
        if(username==null) throw  new BadCredentialsException("invalid");
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("password not match");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

}


