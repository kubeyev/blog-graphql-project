package com.graphqlexample.project.services.services;


import com.graphqlexample.project.models.entities.Comment;
import com.graphqlexample.project.models.dtos.CommentCreateDto;
import com.graphqlexample.project.models.dtos.CommentUpdateDto;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

public interface CommentService {

    public List<Comment> getAllComments();

    public List<Comment> getCommentsByCount(final int count);

    public Comment getComment(final Long id);

    public int countComments();

    public Comment createComment(CommentCreateDto input);

    public Comment updateComment(CommentUpdateDto input) throws ResourceNotFoundException, AccessDeniedException;

    public String deleteComment(Long id);
}
