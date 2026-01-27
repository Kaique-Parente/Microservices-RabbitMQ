package com.microservice.email.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.microservice.email.dtos.EmailRecordDto;
import com.microservice.email.models.EmailModel;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    
    @Mapping(target = "emailId", ignore = true)
    @Mapping(target = "emailFrom", ignore = true)
    @Mapping(target = "sendDateEmail", ignore = true)
    @Mapping(target = "statusEmail", ignore = true)
    EmailModel toEntity(EmailRecordDto emailRecordDto);
}
