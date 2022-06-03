package com.graphqlexample.project.graphql;


import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.dtos.PostUpdateDto;
import com.graphqlexample.project.dtos.PostCreateDto;
import com.graphqlexample.project.services.PostService;
import org.springframework.security.access.annotation.Secured;

@DgsComponent
@RequiredArgsConstructor
public class PostMutation {

  private final PostService postService;

  @DgsMutation
  @Secured("WRITE_ADMIN")
  public Post createPost(@InputArgument PostCreateDto input) {
    return postService.createPost(input);

  }

  @DgsMutation
  @Secured("WRITE_ADMIN")
  public Post updatePost(@InputArgument PostUpdateDto input) {
    return postService.updatePost(input);
  }

  @DgsMutation
  @Secured("WRITE_ADMIN")
  public boolean deletePost(@InputArgument Long id) {
    return postService.deletePost(id);
  }
}
