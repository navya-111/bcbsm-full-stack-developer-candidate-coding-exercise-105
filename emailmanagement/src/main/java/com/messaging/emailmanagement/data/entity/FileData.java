package com.messaging.emailmanagement.data.entity;


import lombok.Data;
import org.bson.types.ObjectId;
@Data
public class FileData {
    private String filename;
    private String objectId;
}
