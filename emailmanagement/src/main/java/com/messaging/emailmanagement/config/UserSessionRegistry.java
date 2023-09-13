package com.messaging.emailmanagement.config;

import com.messaging.emailmanagement.data.model.LoginRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserSessionRegistry {

    private static final Map<String, String> sessions = new HashMap<>();

    public String createSession(final LoginRequest user) {
        if (user == null) {
            throw new RuntimeException("User not specified");
        } else {
            final String sessionId = new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
            sessions.put(user.getEmail(), sessionId);
            return sessions.get(user.getEmail());
        }
    }

    public String getUserNameFromSession(final String sessionId) {

        for (Map.Entry<String,String> entry : sessions.entrySet()) {
            if (entry.getValue().equals(sessionId))
                return entry.getKey();
        }

        return null;
    }

    public String getSessionId(final String loginUser) {
        return sessions.get(loginUser);
    }

    public void removeSession(String loginUser) {
        sessions.remove(loginUser);
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
