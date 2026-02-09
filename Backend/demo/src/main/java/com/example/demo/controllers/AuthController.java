package com.example.demo.controllers;

import com.example.demo.io.LoginRequest;
import com.example.demo.io.TokenResponse;
import com.example.demo.io.UserDTO;
import com.example.demo.security.JWTService;
import com.example.demo.services.AuthService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userDTO));
    }

    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        Authentication authentication = authenticate(loginRequest);
        return ResponseEntity.ok().body(null);
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password()));
    }
}
