package com.graphqlexample.project.graphql;

import lombok.RequiredArgsConstructor;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import com.graphqlexample.project.models.Comment;
import com.graphqlexample.project.dtos.CommentUpdateDto;
import com.graphqlexample.project.dtos.CommentCreateDto;
import com.graphqlexample.project.services.CommentService;
import org.springframework.security.access.annotation.Secured;

@DgsComponent
@RequiredArgsConstructor
public class CommentMutation {

  private final CommentService commentService;

  @DgsMutation
  @Secured({"ROLE_WRITE_ADMIN", "ROLE_WRITE_USER"})
  public Comment createComment(@InputArgument CommentCreateDto input) {
    return commentService.createComment(input);
  }

  @DgsMutation
  @Secured({"ROLE_WRITE_ADMIN", "ROLE_WRITE_USER"})
  public Comment updateComment(@InputArgument CommentUpdateDto input) {
    return commentService.updateComment(input);
  }

  @DgsMutation
  @Secured({"ROLE_WRITE_ADMIN", "ROLE_WRITE_USER"})
  public boolean deleteComment(@InputArgument Long id) {
    return commentService.deleteComment(id);
  }
}
