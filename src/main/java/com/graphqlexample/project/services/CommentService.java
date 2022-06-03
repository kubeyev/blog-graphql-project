package com.graphqlexample.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.graphqlexample.project.models.Comment;
import com.graphqlexample.project.dtos.CommentCreateDto;
import com.graphqlexample.project.dtos.CommentUpdateDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import com.graphqlexample.project.repositories.CommentRepository;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final AuthService authService;
  private final CommentRepository commentRepository;

  @Transactional(readOnly = true)
  public List<Comment> getAllComments() {
    return commentRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Comment> getCommentsByCount(final int count) {
    return commentRepository.findAll().stream().limit(count).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Comment getComment(final Long id) {
    return commentRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found!"));
  }

  @Transactional(readOnly = true)
  public int countComments() {
    return (int) commentRepository.count();
  }

  @Transactional
  public Comment createComment(CommentCreateDto input) {
    return commentRepository.save(new Comment(
            input.getContent(),
            LocalDate.parse(input.getPublishedDate()),
            input.getPost(),
            authService.getCurrentUser()
    ));
  }

  public Comment updateComment(CommentUpdateDto input) {
    var comment = commentRepository.findById(input.getId())
      .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + input.getId() + " not found!"));
    if (authService.ownerUser(comment.getUser()) ||
                    authService.getCurrentUser().getRoles().contains("WRITE_ADMIN")) {
      comment.setContent(input.getContent());
      comment.setPublishedDate(LocalDate.parse(input.getPublishedDate()));
      return commentRepository.save(comment);
    }
    else {
      throw new AccessDeniedException("This user has no permision to update comment!");
    }
  }

  public boolean deleteComment(Long id) {
    var comment = commentRepository.findById(id);
    if (comment.isPresent() & (
            authService.ownerUser(comment.get().getUser()) ||
                    authService.getCurrentUser().getRoles().contains("WRITE_ADMIN"))) {
      commentRepository.deleteById(id);
      return true;
    } else return false;
  }
}
