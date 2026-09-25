package com.dsa.assistant.service;

import com.dsa.assistant.dto.AuthResponse;
import com.dsa.assistant.dto.LoginRequest;
import com.dsa.assistant.dto.RegisterRequest;
import com.dsa.assistant.model.User;
import com.dsa.assistant.repository.UserRepository;
import com.dsa.assistant.security.CurrentUser;
import com.dsa.assistant.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(CurrentUser.fromUser(savedUser));

        return new AuthResponse(token, "Bearer", savedUser.getId(), savedUser.getName(), savedUser.getEmail(), jwtService.getExpiryTime());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + principal.getUsername()));

        String token = jwtService.generateToken(CurrentUser.fromUser(user));
        return new AuthResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail(), jwtService.getExpiryTime());
    }
}