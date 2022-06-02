package com.graphqlexample.project.graphql;

import lombok.RequiredArgsConstructor;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.services.PostService;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class PostQuery {
  private final PostService postService;

  @DgsQuery
  public List<Post> getAllPosts() {
    return postService.getAllPosts();
  }

  @DgsQuery
  public List<Post> getPostsByCount(@InputArgument final int count) {
    return postService.getPostsByCount(count);
  }

  @DgsQuery
  public Post getPost(final Long id) {
    return postService.getPost(id);
  }

  @DgsQuery
  public int countPosts() {
    return postService.countPosts();
  }
}
