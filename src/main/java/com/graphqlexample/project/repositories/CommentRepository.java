package com.graphqlexample.project.repositories;

import com.graphqlexample.project.models.entities.Comment;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
