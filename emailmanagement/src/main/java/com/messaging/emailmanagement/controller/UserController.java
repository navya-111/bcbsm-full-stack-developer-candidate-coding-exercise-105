package com.messaging.emailmanagement.controller;

import com.messaging.emailmanagement.data.entity.User;
import com.messaging.emailmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4200")
    public void addUser(@Validated @RequestBody User user) throws Exception {
        userService.addUser(user);
    }
}
