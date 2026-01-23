package com.microservice.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.dto.LoginRequestDTO;
import com.microservice.user.dto.LoginResponseDTO;
import com.microservice.user.services.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {
    
    private final LoginService loginService;

    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(loginService.login(loginRequestDTO));
    }

}
