package com.graphqlexample.project.graphql;


import com.graphqlexample.project.dtos.PostUpdateDto;
import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import graphql.schema.DataFetchingEnvironment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.dtos.PostCreateDto;
import com.graphqlexample.project.services.PostService;

@DgsComponent
@RequiredArgsConstructor
public class PostMutation {

  private final PostService postService;
  private final ObjectMapper objectMapper;

  @DgsMutation
  public Post createPost(DataFetchingEnvironment environment) {
    var input = objectMapper.convertValue(environment.getArgument("input"), PostCreateDto.class);
    System.out.println("This is the input " + input);
    return this.postService.createPost(input);
  }

  @DgsMutation
  public Post updatePost(@InputArgument PostUpdateDto input) {
    return this.postService.updatePost(input);
  }

  @DgsMutation
  public boolean deletePost(@InputArgument Long id) {
    return this.postService.deletePost(id);
  }
}
