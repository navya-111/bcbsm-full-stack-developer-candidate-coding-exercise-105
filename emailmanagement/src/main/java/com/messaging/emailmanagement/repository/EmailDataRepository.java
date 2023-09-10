package com.messaging.emailmanagement.repository;

import com.messaging.emailmanagement.data.entity.EmailData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmailDataRepository extends MongoRepository<EmailData, String> {
    List<EmailData> findByFromEmail(String email);
}
