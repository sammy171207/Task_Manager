package com.example.taskmanagement;

import com.example.taskmanagement.config.JwtProvider;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserServiceImp userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("ROLE_USER");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_NotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        User result = userService.findByUsername("testuser");

        assertNull(result);
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUserByJwtToken() throws Exception {
        String jwt = "sample.jwt.token";
        String username = "testuser";

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("ROLE_USER");

        when(jwtProvider.getUsernameFromJwtToken(jwt)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.findByUserByJwtToken(jwt);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(jwtProvider, times(1)).getUsernameFromJwtToken(jwt);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUserByJwtToken_UserNotFound() {
        String jwt = "sample.jwt.token";
        String username = "testuser";

        when(jwtProvider.getUsernameFromJwtToken(jwt)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            userService.findByUserByJwtToken(jwt);
        });

        assertEquals("User not found", exception.getMessage());
        verify(jwtProvider, times(1)).getUsernameFromJwtToken(jwt);
        verify(userRepository, times(1)).findByUsername(username);
    }

}
