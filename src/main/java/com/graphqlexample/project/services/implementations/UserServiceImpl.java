package com.graphqlexample.project.services.implementations;

import com.graphqlexample.project.services.services.UserService;
import lombok.RequiredArgsConstructor;
import com.graphqlexample.project.models.entities.Role;
import com.graphqlexample.project.models.entities.User;
import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public User createUser(User user) {
    String[] roleNames = {"READ_USER", "WRITE_USER"};
    Set<Role> roleSet = createRoleSets(roleNames);
    user.setRoles(roleSet);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  public User createAdminUser(User user) {
    String[] roleNames = {"READ_ADMIN", "WRITE_ADMIN", "READ_USER", "WRITE_USER"};
    Set<Role> roleSet = createRoleSets(roleNames);
    user.setRoles(roleSet);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  private Set<Role> createRoleSets(String[] roleList) {
    var roles = new HashSet<Role>();
    for (String roleName:roleList) {
      var role = roleRepository.findByName(roleName);
      roles.add(role);
    }
    return roles;
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public User findByLoginAndPassword(String username, String password) {
    var user = findByUsername(username);
    if (user != null) {
      if (passwordEncoder.matches(password, user.getPassword())) {
        return user;
      }
    }
    return null;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return findByUsername(username);
  }
}
