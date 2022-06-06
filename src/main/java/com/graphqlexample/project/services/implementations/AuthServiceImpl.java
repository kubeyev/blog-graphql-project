package com.graphqlexample.project.services.implementations;

import com.graphqlexample.project.services.services.AuthService;
import org.springframework.stereotype.Service;
import com.graphqlexample.project.models.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public boolean ownerUser(User user) {
        return user == getCurrentUser();
    }
}
