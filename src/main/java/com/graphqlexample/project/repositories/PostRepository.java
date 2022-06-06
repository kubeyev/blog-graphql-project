package com.graphqlexample.project.repositories;

import com.graphqlexample.project.models.entities.Post;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
