package com.graphqlexample.project.graphql;


import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import com.graphqlexample.project.models.entities.Post;
import com.graphqlexample.project.models.dtos.PostUpdateDto;
import com.graphqlexample.project.models.dtos.PostCreateDto;
import com.graphqlexample.project.services.implementations.PostServiceImpl;
import org.springframework.security.access.annotation.Secured;

@DgsComponent
@RequiredArgsConstructor
public class PostMutation {

  private final PostServiceImpl postServiceImpl;

  @DgsMutation
  @Secured("ROLE_WRITE_ADMIN")
  public Post createPost(@InputArgument PostCreateDto input) {
    return postServiceImpl.createPost(input);
  }

  @DgsMutation
  @Secured("ROLE_WRITE_ADMIN")
  public Post updatePost(@InputArgument PostUpdateDto input) {
    return postServiceImpl.updatePost(input);
  }

  @DgsMutation
  @Secured("ROLE_WRITE_ADMIN")
  public void deletePost(@InputArgument Long id) {
    postServiceImpl.deletePost(id);
  }
}
