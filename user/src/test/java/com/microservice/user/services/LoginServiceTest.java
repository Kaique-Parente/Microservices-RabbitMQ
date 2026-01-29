package com.microservice.user.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.microservice.user.dto.LoginRequestDto;
import com.microservice.user.models.UserModel;
import com.microservice.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtEncoder jwtEncoder;

    @InjectMocks
    LoginService loginService;

    @Test
    void shouldThrowExceptionWhenUserEmailNotExists(){

        var requestDto = new LoginRequestDto("email@email.com", "333");

        when(userRepository.findByEmail(requestDto.email()))
            .thenReturn(Optional.empty());
        
        assertThrows(ResponseStatusException.class, () -> {
            loginService.login(requestDto);
        });

    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect(){

        var user = UserModel.builder()
            .name("TesteName")
            .email("email@mail.com")
            .password(passwordEncoder.encode("333"))
            .build();
        
        var requestDto = new LoginRequestDto("email@mail.com", "111111");

        when(userRepository.findByEmail(requestDto.email()))
            .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(requestDto.password(), user.getPassword()))
            .thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            loginService.login(requestDto);
        });

    }
}
