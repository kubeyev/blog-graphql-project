package com.graphqlexample.project.services;

import org.springframework.stereotype.Service;
import com.graphqlexample.project.models.User;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class AuthService {
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean ownerUser(User user) {
        return user == getCurrentUser();
    }
}
