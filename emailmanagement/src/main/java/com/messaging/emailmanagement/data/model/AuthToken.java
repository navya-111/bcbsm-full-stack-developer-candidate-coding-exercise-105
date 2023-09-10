package com.messaging.emailmanagement.data.model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthToken {

    private String token;

    private String email;

    private String userName;
}