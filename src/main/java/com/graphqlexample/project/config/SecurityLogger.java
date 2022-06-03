package com.graphqlexample.project.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

import java.util.Optional;

@Slf4j
@Component
public class SecurityLogger {

    @EventListener
    public void authenticated(final @NotNull AuthenticationSuccessEvent event) {
        final Object principal = event.getAuthentication().getPrincipal();
        String username = extractUsername(principal);
        log.info("Successful login - [username: \"{}\"]", username);
    }

    @EventListener
    public void authenticationFailure(final @NotNull AbstractAuthenticationFailureEvent event) {
        final Object principal = event.getAuthentication().getPrincipal();
        String username = extractUsername(principal);
        log.info("Unsuccessful login - [username: \"{}\"]", username);
    }

    @EventListener
    public void authorizationFailure(final @NotNull AuthorizationFailureEvent event) {
        final Object principal = event.getAuthentication().getPrincipal();
        String username = extractUsername(principal);
        String message = event.getAccessDeniedException().getMessage();
        Object resource = event.getSource();
        log.info("Unauthorized access - [username: \"{}\", message: \"{}\", resource: \"{}\"]",
                username, Optional.ofNullable(message).orElse("<null>"),
                Optional.ofNullable(resource).orElse("<null>"));
    }

    @EventListener
    public void logoutSuccess(final @NotNull LogoutSuccessEvent event) {
        final Object principal = event.getAuthentication().getPrincipal();
        String username = extractUsername(principal);
        log.info("Successful logout - [username: \"{}\"]", username);
    }

    private String extractUsername(Object principal) {
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
