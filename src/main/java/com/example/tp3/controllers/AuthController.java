package com.example.tp3.controllers;

import com.example.tp3.dto.JwtResponse;
import com.example.tp3.dto.LoginRequest;
import com.example.tp3.dto.RegisterRequest;
import com.example.tp3.models.User;
import com.example.tp3.repositories.UserRepository;
import com.example.tp3.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo, PasswordEncoder encoder,AuthenticationManager authenticationManager,JwtUtil jwtUtil) {
        this.userRepo = userRepo; this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            return "Username already exists";
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(req.getRole().toUpperCase());
        userRepo.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        var user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        var role = user.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        String jwt = jwtUtil.generateToken(user.getUsername(), role);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
