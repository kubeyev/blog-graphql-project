package com.graphqlexample.project.services.services;

import com.graphqlexample.project.models.entities.User;

public interface AuthService {
    public User getCurrentUser();

    public boolean ownerUser(User user);
}
