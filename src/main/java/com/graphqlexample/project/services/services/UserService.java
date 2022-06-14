package com.graphqlexample.project.services.services;

import com.graphqlexample.project.models.entities.User;

public interface UserService {
    public User createUser(User user);

    public User findByUsername(String username);

    public User findByLoginAndPassword(String username, String password);

    public User createAdminUser(User user);
}
