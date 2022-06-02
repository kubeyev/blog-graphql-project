package com.graphqlexample.project.graphql;

import com.graphqlexample.project.dtos.CommentCreateDto;
import com.graphqlexample.project.dtos.CommentUpdateDto;
import com.graphqlexample.project.models.Comment;
import com.graphqlexample.project.services.CommentService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

@DgsComponent
@RequiredArgsConstructor
public class CommentMutation {

  private final CommentService commentService;

  @DgsMutation
  public Comment createComment(@InputArgument CommentCreateDto input) {
    return commentService.createComment(input);
  }

  @DgsMutation
  public Comment updateComment(@InputArgument CommentUpdateDto input) {
    return commentService.updateComment(input);
  }

  @DgsMutation
  public boolean deleteComment(@InputArgument Long id) {
    return commentService.deleteComment(id);
  }
}
