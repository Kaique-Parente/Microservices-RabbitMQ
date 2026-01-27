package com.microservice.email.services;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.microservice.email.models.EmailModel;

@Service
public class SendEmailService {
    
    private final JavaMailSender emailSender;

    public SendEmailService(JavaMailSender emailSender){
        this.emailSender = emailSender;
    }

    public void sendEmail(EmailModel emailModel) throws MailException{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailModel.getEmailTo());
        message.setSubject(emailModel.getSubject());
        message.setText(emailModel.getText());
        emailSender.send(message);
    }

}
