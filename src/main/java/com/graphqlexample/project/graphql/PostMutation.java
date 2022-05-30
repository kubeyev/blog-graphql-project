package com.graphqlexample.project.graphql;


import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import graphql.schema.DataFetchingEnvironment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.inputs.PostInput;
import com.graphqlexample.project.services.PostService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@DgsComponent
@RequiredArgsConstructor
public class PostMutation {

  private final PostService postService;
  private final ObjectMapper objectMapper;

  @DgsMutation(field = "createPost")
  public Post createPost(DataFetchingEnvironment environment) {
    var input = objectMapper.convertValue(environment.getArgument("input"), PostInput.class);
    return this.postService.createPost(input.getTitle(), input.getContent(), input.getPublishedDate());
  }

  public Post updatePost(final Long id, final String title, final String content) {
    return this.postService.updatePost(id, title, content);
  }

  public boolean deletePost(Long id) {
    return this.postService.deletePost(id);
  }
}
