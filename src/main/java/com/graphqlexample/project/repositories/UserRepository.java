package com.graphqlexample.project.repositories;

import com.graphqlexample.project.models.entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String login);
}
