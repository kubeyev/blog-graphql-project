package com.graphqlexample.project.repositories;

import com.graphqlexample.project.models.entities.Role;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findFirstByName(String name);
}
