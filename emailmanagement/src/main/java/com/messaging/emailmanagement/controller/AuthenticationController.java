package com.messaging.emailmanagement.controller;

import com.messaging.emailmanagement.config.EncryptionUtil;
import com.messaging.emailmanagement.config.UserSessionRegistry;
import com.messaging.emailmanagement.data.entity.User;
import com.messaging.emailmanagement.data.model.AuthToken;
import com.messaging.emailmanagement.data.model.LoginRequest;
import com.messaging.emailmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserSessionRegistry sessionRegistry;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody LoginRequest loginRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), EncryptionUtil.encrypt(loginRequest.getPassword())));
        String sessionId = sessionRegistry.createSession(loginRequest);
        User user = userService.findById(loginRequest.getEmail());
        AuthToken authToken = new AuthToken(sessionId, loginRequest.getEmail(), user.getName());
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(name = "userId") String loginUser) {
        sessionRegistry.removeSession(loginUser);
    }
}
