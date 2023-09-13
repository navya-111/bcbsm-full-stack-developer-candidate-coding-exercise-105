package com.messaging.emailmanagement.data.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document(collection = "emaildata")
public class EmailData implements Serializable {
    @MongoId
    private String id;

    private String fromEmail;

    private String toEmail;

    private String subject;

    private String message;

    private FileData fileData;

    private LocalDateTime uploadDate;
}
