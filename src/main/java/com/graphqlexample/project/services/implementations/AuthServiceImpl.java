package com.graphqlexample.project.services.implementations;

import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.services.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.graphqlexample.project.models.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    public final RoleRepository roleRepository;

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public boolean isOwnerUser(User user) {
        return user.getId() == getCurrentUser().getId();
    }

    @Override
    public boolean isAdminUser() {
        return getCurrentUser().getRoles().contains(roleRepository.findFirstByName("WRITE_ADMIN"));
    }
}
