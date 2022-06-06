package com.graphqlexample.project.graphql;

import lombok.RequiredArgsConstructor;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import com.graphqlexample.project.models.entities.Post;
import com.graphqlexample.project.services.implementations.PostServiceImpl;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class PostQuery {
  private final PostServiceImpl postServiceImpl;

  @DgsQuery
  public List<Post> getAllPosts() {
    return postServiceImpl.getAllPosts();
  }

  @DgsQuery
  public List<Post> getPostsByCount(@InputArgument final int count) {
    return postServiceImpl.getPostsByCount(count);
  }

  @DgsQuery
  public Post getPost(final Long id) {
    return postServiceImpl.getPost(id);
  }

  @DgsQuery
  public int countPosts() {
    return postServiceImpl.countPosts();
  }
}
