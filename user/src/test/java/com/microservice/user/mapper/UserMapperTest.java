package com.microservice.user.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservice.user.dto.CreateUserDto;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapCreateUserDtoToEntityIgnoringFields(){
        var userDto = new CreateUserDto("Teste", "email@email.com", "33333");

        var userConvertWithMapper = userMapper.toUserEntity(userDto);

        assertEquals("Teste", userConvertWithMapper.getName());
        assertEquals("email@email.com", userConvertWithMapper.getEmail());

        assertNull(userConvertWithMapper.getUserId());
        assertNull(userConvertWithMapper.getPassword());
        assertNull(userConvertWithMapper.getRoles());
    }

}
