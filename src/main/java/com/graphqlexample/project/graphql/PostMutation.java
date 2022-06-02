package com.graphqlexample.project.graphql;


import com.graphqlexample.project.dtos.PostUpdateDto;
import com.graphqlexample.project.services.AuthService;
import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import graphql.schema.DataFetchingEnvironment;
import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.dtos.PostCreateDto;
import com.graphqlexample.project.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class PostMutation {

  private final PostService postService;
  private final AuthService authService;

  @DgsMutation
  @Secured("ROLE_ADMIN")
  public Post createPost(@InputArgument PostCreateDto input) {
    var user = authService.getCurrentUser();
    log.info("User {} is creating post", user.getUsername());
    return postService.createPost(input);

  }

  @DgsMutation
  public Post updatePost(@InputArgument PostUpdateDto input) {
    return postService.updatePost(input);
  }

  @DgsMutation
  public boolean deletePost(@InputArgument Long id) {
    return postService.deletePost(id);
  }
}
