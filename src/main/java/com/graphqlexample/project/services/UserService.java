package com.graphqlexample.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.graphqlexample.project.models.User;
import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  public User createUser(User user) {
    var role = roleRepository.findByName("ROLE_USER");
    user.setRole(role);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

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
