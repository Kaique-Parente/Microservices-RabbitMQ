package com.microservice.email.usecase;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import com.microservice.email.enums.StatusEmail;
import com.microservice.email.models.EmailModel;
import com.microservice.email.services.EmailService;
import com.microservice.email.services.SendEmailService;

@Component
public class SendEmailUseCase {
    
    private final EmailService emailService;
    private final SendEmailService sendEmailService;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public SendEmailUseCase(EmailService emailService, SendEmailService sendEmailService){
        this.emailService = emailService;
        this.sendEmailService = sendEmailService;
    }

    public EmailModel execute(EmailModel emailModel){
        try{
            emailModel.setSendDateEmail(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);
            
            sendEmailService.sendEmail(emailModel);
            emailModel.setStatusEmail(StatusEmail.SENT);
        }catch(MailException e){
            emailModel.setStatusEmail(StatusEmail.ERROR);
        }

        return emailService.save(emailModel);
    }

}
