package com.graphqlexample.project.services.implementations;

import com.graphqlexample.project.exceptions.ResourceNotFoundException;
import com.graphqlexample.project.services.services.UserService;
import lombok.RequiredArgsConstructor;
import com.graphqlexample.project.models.entities.Role;
import com.graphqlexample.project.models.entities.User;
import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public User createUser(User user) {
    String[] roleNames = {"READ_USER", "WRITE_USER"};
    user.setRoles(createRoleSets(roleNames));
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public User createAdminUser(User user) {
    String[] roleNames = {"READ_ADMIN", "WRITE_ADMIN", "READ_USER", "WRITE_USER"};
    user.setRoles(createRoleSets(roleNames));
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  private Set<Role> createRoleSets(String[] roleList) {
    var roles = new HashSet<Role>();
    for (String roleName:roleList) {
      Role role = roleRepository.findFirstByName(roleName)
              .orElse(roleRepository.save(new Role(roleName)));
      roles.add(role);
    }
    return roles;
  }

  @Override
  public User findByUsername(String username) throws UsernameNotFoundException{
    User user = userRepository.findFirstByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    return user;
  }

  @Override
  public User findByLoginAndPassword(String username, String password) throws AccessDeniedException {
    var user = findByUsername(username);
    if (passwordEncoder.matches(password, user.getPassword())) {
        return user;
    }
    else {
      throw new AccessDeniedException("Invalid user password");
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return findByUsername(username);
  }
}
