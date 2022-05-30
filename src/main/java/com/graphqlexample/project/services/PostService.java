package com.graphqlexample.project.services;

import com.graphqlexample.project.exceptions.PostNotFoundException;
import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RestController
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  @Transactional
  public Post createPost(final String title, final String content, final String publishedDate) {
    return postRepository.save(new Post(title, content, LocalDate.parse(publishedDate)));
  }
  @Transactional(readOnly = true)
  public List<Post> getAllPosts(final int count) {
    return this.postRepository.findAll().stream().limit(count).collect(Collectors.toList());
  }
  @Transactional(readOnly = true)
  public Post getPost(final Long id) {
    return postRepository.findById(id)
      .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found!"));
  }

  @Transactional(readOnly = true)
  public int countPosts() {
    return (int) this.postRepository.count();
  }

  public Post updatePost(Long id, String title, String content) {
    Post post = postRepository.findById(id)
      .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found!"));
    post.setTitle(title);
    post.setContent(content);
    return post;
  }

  public boolean deletePost(Long id) {
    Optional<Post> post = postRepository.findById(id);
    if (post.isPresent()) {
      postRepository.deleteById(id);
      return true;
    } else return false;
  }
}
