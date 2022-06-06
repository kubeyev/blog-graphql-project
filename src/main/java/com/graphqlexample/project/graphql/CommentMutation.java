package com.graphqlexample.project.graphql;

import lombok.RequiredArgsConstructor;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import com.graphqlexample.project.models.entities.Comment;
import com.graphqlexample.project.models.dtos.CommentUpdateDto;
import com.graphqlexample.project.models.dtos.CommentCreateDto;
import com.graphqlexample.project.services.implementations.CommentServiceImpl;
import org.springframework.security.access.annotation.Secured;

@DgsComponent
@RequiredArgsConstructor
public class CommentMutation {

  private final CommentServiceImpl commentServiceImpl;

  @DgsMutation
  @Secured({"ROLE_WRITE_ADMIN", "ROLE_WRITE_USER"})
  public Comment createComment(@InputArgument CommentCreateDto input) {
    return commentServiceImpl.createComment(input);
  }

  @DgsMutation
  @Secured({"ROLE_WRITE_ADMIN", "ROLE_WRITE_USER"})
  public Comment updateComment(@InputArgument CommentUpdateDto input) {
    return commentServiceImpl.updateComment(input);
  }

  @DgsMutation
  @Secured({"ROLE_WRITE_ADMIN", "ROLE_WRITE_USER"})
  public boolean deleteComment(@InputArgument Long id) {
    return commentServiceImpl.deleteComment(id);
  }
}
