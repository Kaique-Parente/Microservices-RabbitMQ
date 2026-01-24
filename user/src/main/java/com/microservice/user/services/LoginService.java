package com.microservice.user.services;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.microservice.user.dto.LoginRequestDto;
import com.microservice.user.dto.LoginResponseDto;
import com.microservice.user.models.RoleModel;
import com.microservice.user.repository.UserRepository;

import jakarta.transaction.Transactional;

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
    public LoginResponseDto login(LoginRequestDto loginRequestDTO){

        var user = userRepository.findByEmail(loginRequestDTO.email());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequestDTO, passwordEncoder)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email incorreto ou senha inválida!");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.get().getRoles()
                            .stream()
                            .map(RoleModel::getName)
                            .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                        .issuer("Microsservico_user")
                        .subject(user.get().getUserId().toString())
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(expiresIn))
                        .claim("scope", scopes)
                        .build();
        
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDto(jwtValue, expiresIn);
    }
}
