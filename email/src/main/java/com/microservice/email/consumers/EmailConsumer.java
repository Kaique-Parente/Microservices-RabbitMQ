package com.microservice.email.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.microservice.email.dtos.EmailRecordDto;
import com.microservice.email.mapper.EmailMapper;
import com.microservice.email.usecase.SendEmailUseCase;

@Component
public class EmailConsumer {

    private final SendEmailUseCase sendEmailUseCase;
    private final EmailMapper emailMapper;

    public EmailConsumer(SendEmailUseCase sendEmailUseCase, EmailMapper emailMapper){
        this.sendEmailUseCase = sendEmailUseCase;
        this.emailMapper = emailMapper;
    }
    
    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailRecordDto emailRecordDto){
        var emailModel = emailMapper.toEntity(emailRecordDto);
        sendEmailUseCase.execute(emailModel);
    }
    
}
