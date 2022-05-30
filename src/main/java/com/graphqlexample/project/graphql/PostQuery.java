package com.graphqlexample.project.graphql;

import com.netflix.graphql.dgs.DgsQuery;
import lombok.RequiredArgsConstructor;
import com.netflix.graphql.dgs.DgsComponent;
import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.services.PostService;

import java.util.List;
import java.util.Optional;

@DgsComponent
@RequiredArgsConstructor
public class PostQuery {
  private final PostService postService;

  @DgsQuery(field = "findAllPosts")
  public List<Post> findAllPosts(final int count) {
    return this.postService.getAllPosts(count);
  }

  @DgsQuery(field = "getPost")
  public Post getPost(final Long id) {
    return this.postService.getPost(id);
  }

  public int countPosts() {
    return postService.countPosts();
  }
}
