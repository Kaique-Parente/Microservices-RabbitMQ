package com.microservice.user.repository;

import java.util.Optional;
import java.util.UUID;

import com.microservice.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByEmail(String email);
    
}
