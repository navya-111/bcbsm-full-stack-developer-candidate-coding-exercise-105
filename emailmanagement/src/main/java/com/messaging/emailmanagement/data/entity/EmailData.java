package com.messaging.emailmanagement.data.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
