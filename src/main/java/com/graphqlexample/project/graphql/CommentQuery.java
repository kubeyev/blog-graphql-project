package com.graphqlexample.project.graphql;

import com.graphqlexample.project.models.Comment;
import com.graphqlexample.project.services.CommentService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class CommentQuery {
  private final CommentService commentService;

  @DgsQuery
  public List<Comment> getAllComments() {
    return commentService.getAllComments();
  }

  @DgsQuery
  public List<Comment> getCommentsByCount(@InputArgument final int count) {
    return commentService.getCommentsByCount(count);
  }

  @DgsQuery
  public Comment getComment(final Long id) {
    return commentService.getComment(id);
  }

  @DgsQuery
  public int countComments() {
    return commentService.countComments();
  }
}
