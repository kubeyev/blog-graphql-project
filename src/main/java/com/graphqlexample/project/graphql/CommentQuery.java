package com.graphqlexample.project.graphql;

import lombok.RequiredArgsConstructor;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import com.graphqlexample.project.models.entities.Comment;
import com.graphqlexample.project.services.implementations.CommentServiceImpl;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class CommentQuery {
  private final CommentServiceImpl commentServiceImpl;

  @DgsQuery
  public List<Comment> getAllComments() {
    return commentServiceImpl.getAllComments();
  }

  @DgsQuery
  public List<Comment> getCommentsByCount(@InputArgument final int count) {
    return commentServiceImpl.getCommentsByCount(count);
  }

  @DgsQuery
  public Comment getComment(final Long id) {
    return commentServiceImpl.getComment(id);
  }

  @DgsQuery
  public int countComments() {
    return commentServiceImpl.countComments();
  }
}
