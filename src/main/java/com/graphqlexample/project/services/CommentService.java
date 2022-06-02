package com.graphqlexample.project.services;

import com.graphqlexample.project.dtos.CommentCreateDto;
import com.graphqlexample.project.dtos.CommentUpdateDto;
import com.graphqlexample.project.dtos.PostCreateDto;
import com.graphqlexample.project.dtos.PostUpdateDto;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;
import com.graphqlexample.project.models.Comment;
import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.repositories.CommentRepository;
import com.graphqlexample.project.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
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
    return commentRepository.save(new Comment(input.getContent(), LocalDate.parse(input.getPublishedDate()), input.getPost()));
  }

  public Comment updateComment(CommentUpdateDto input) {
    var comment = commentRepository.findById(input.getId())
      .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + input.getId() + " not found!"));
    comment.setContent(input.getContent());
    comment.setPublishedDate(LocalDate.parse(input.getPublishedDate()));
    return commentRepository.save(comment);
  }

  public boolean deleteComment(Long id) {
    var post = commentRepository.findById(id);
    if (post.isPresent()) {
      commentRepository.deleteById(id);
      return true;
    } else return false;
  }
}
