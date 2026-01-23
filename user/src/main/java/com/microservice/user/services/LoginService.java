package com.microservice.user.services;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.microservice.user.dto.LoginRequestDTO;
import com.microservice.user.dto.LoginResponseDTO;
import com.microservice.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class LoginService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional
    public LoginResponseDTO login(@Valid LoginRequestDTO loginRequestDTO){

        var user = userRepository.findByEmail(loginRequestDTO.email());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequestDTO, passwordEncoder)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email incorreto ou senha inválida!");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var claims = JwtClaimsSet.builder()
                        .issuer("Microsservico_user")
                        .subject(user.get().getUserId().toString())
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(expiresIn))
                        .build();
        
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }
}
