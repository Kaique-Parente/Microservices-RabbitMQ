package com.microservice.email.services;

import org.springframework.stereotype.Service;
import com.microservice.email.models.EmailModel;
import com.microservice.email.repositories.EmailRepository;

@Service
public class EmailService {

    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public EmailModel save(EmailModel emailModel){
        return emailRepository.save(emailModel);
    }

}
