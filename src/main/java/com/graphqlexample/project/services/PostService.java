package com.graphqlexample.project.services;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.dtos.PostCreateDto;
import com.graphqlexample.project.dtos.PostUpdateDto;
import com.graphqlexample.project.repositories.PostRepository;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RestController
@RequiredArgsConstructor
public class PostService {

  private final AuthService authService;
  private final PostRepository postRepository;
  @Transactional(readOnly = true)
  public List<Post> getAllPosts() {
    return postRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Post> getPostsByCount(final int count) {
    return postRepository.findAll().stream().limit(count).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Post getPost(final Long id) {
    return postRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found!"));
  }

  @Transactional(readOnly = true)
  public int countPosts() {
    return (int) postRepository.count();
  }

  @Transactional
  public Post createPost(PostCreateDto input) {
    var user = authService.getCurrentUser();
    log.info("User {} is creating post", user.getUsername());
    return postRepository.save(
            new Post(
                    input.getTitle(),
                    input.getContent(),
                    LocalDate.parse(input.getPublishedDate()),
                    user
            ));
  }

  public Post updatePost(PostUpdateDto input) throws AccessDeniedException, ResourceNotFoundException{
    Post post = postRepository.findById(input.getId())
      .orElseThrow(() -> new ResourceNotFoundException("Post with id " + input.getId() + " not found!"));
    post.setTitle(input.getTitle());
    post.setContent(input.getContent());
    post.setPublishedDate(LocalDate.parse(input.getPublishedDate()));
    return postRepository.save(post);
  }

  public boolean deletePost(Long id) {
    Optional<Post> post = postRepository.findById(id);
    if (post.isPresent()) {
      postRepository.deleteById(id);
      return true;
    }
    else {
      return false;
    }
  }
}
