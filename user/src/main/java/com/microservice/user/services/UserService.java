package com.microservice.user.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.microservice.user.dto.CreateUserDto;
import com.microservice.user.dto.UserResponseDto;
import com.microservice.user.models.UserModel;
import com.microservice.user.producers.UserProducer;
import com.microservice.user.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final UserProducer userProducer;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserProducer userProducer, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userProducer = userProducer;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto save(CreateUserDto userDto){
        var userDB = userRepository.findByEmail(userDto.email());

        if (userDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse email já existe no nosso sistema!");
        }

        var user = UserModel.builder()
            .name(userDto.name())
            .email(userDto.email())
            .password(passwordEncoder.encode(userDto.password()))
            .build();

        userRepository.save(user);
        userProducer.publishMessageEmail(user);
        return new UserResponseDto(user.getName(), user.getEmail());
    }

    public List<UserModel> getUsers(){
       return userRepository.findAll();
    }

}
