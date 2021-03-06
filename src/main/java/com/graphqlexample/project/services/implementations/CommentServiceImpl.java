package com.graphqlexample.project.services.implementations;

import lombok.RequiredArgsConstructor;
import com.graphqlexample.project.models.entities.Comment;
import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.models.dtos.CommentCreateDto;
import com.graphqlexample.project.models.dtos.CommentUpdateDto;
import com.graphqlexample.project.repositories.CommentRepository;
import com.graphqlexample.project.services.services.CommentService;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  private final AuthServiceImpl authServiceImpl;
  private final CommentRepository commentRepository;

  private final RoleRepository roleRepository;

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
            authServiceImpl.getCurrentUser()
    ));
  }

  public Comment updateComment(CommentUpdateDto input) {
    var comment = commentRepository.findById(input.getId())
      .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + input.getId() + " not found!"));
    if (authServiceImpl.isOwnerUser(comment.getUser()) ||
                    authServiceImpl.isAdminUser()) {
      comment.setContent(input.getContent());
      comment.setPublishedDate(LocalDate.parse(input.getPublishedDate()));
      return commentRepository.save(comment);
    }
    else {
      throw new AccessDeniedException("This user has no permision to update comment!");
    }
  }

  public String deleteComment(Long id) {
    var comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found!"));

    if (authServiceImpl.isOwnerUser(comment.getUser()) || authServiceImpl.isAdminUser()) {
      commentRepository.deleteById(id);
    }
    else {
      throw new AccessDeniedException("This user has no permision to delete comment!");
    }
    return "Comment deleted successfully by " + authServiceImpl.getCurrentUser().getUsername();
  }
}
