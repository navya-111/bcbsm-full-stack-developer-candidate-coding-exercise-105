package com.messaging.emailmanagement.repository;


import com.messaging.emailmanagement.data.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
