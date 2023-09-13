package com.messaging.emailmanagement.data.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Document(collection = "user")
@Data
public class User implements Serializable {
    @MongoId
    private String email;

    private String password;

    private String name;
}