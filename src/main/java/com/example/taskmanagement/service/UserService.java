package com.example.taskmanagement.service;



import com.example.taskmanagement.model.User;

public interface UserService {
    public User saveUser(User user) throws Exception;


   public User findByUsername(String username) throws Exception;

    public User findByUserByJwtToken(String jwt) throws Exception;
}
