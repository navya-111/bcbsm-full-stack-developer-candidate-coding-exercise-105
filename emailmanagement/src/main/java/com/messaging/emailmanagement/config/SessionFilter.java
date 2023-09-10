package com.messaging.emailmanagement.config;

import com.messaging.emailmanagement.data.entity.User;
import com.messaging.emailmanagement.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SessionFilter extends OncePerRequestFilter {

    private  final UserSessionRegistry sessionRegistry;

    private final UserService userService;

    public SessionFilter(final UserSessionRegistry userSessionRegistry, UserService userService) {
        this.sessionRegistry = userSessionRegistry;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (sessionId == null || sessionId.length() == 0) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userName = sessionRegistry.getUserNameFromSession(sessionId);
        if (userName == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final User user = userService.findById(userName);

        UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                userService.getAuthority()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}