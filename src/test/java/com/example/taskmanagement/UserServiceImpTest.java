package com.example.taskmanagement;

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

    @InjectMocks
    private UserServiceImp userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
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
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("ROLE_USER");

        // Mock userRepository.findByUsername to return Optional.of(user)
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Call loadUserByUsername with "testuser"
        UserDetails userDetails = userService.loadUserByUsername("testuser");

        // Assertions
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());

    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("testuser");
        });
    }
}
