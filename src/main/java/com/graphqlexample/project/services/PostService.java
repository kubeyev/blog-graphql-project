package com.graphqlexample.project.services;

import com.graphqlexample.project.models.Post;
import com.graphqlexample.project.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  @Transactional
  public Post createPost(final String title, final String content, final String publishedDate) {
    Post _post = new Post();
    _post.setTitle(title);
    _post.setContent(content);
    _post.setPublishedDate(LocalDate.parse(publishedDate));
    return this.postRepository.save(_post);
  }
  @Transactional(readOnly = true)
  public List<Post> getAllPosts(final int count) {
    return this.postRepository.findAll().stream().limit(count).collect(Collectors.toList());
  }
  @Transactional(readOnly = true)
  public Optional<Post> getPost(final Long id) {
    return this.postRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public int countPosts() {
    return (int) this.postRepository.count();
  }

  public Post updatePost(Long id, String title, String content) throws NotFoundException {
    Optional<Post> postOpt = postRepository.findById(id);
    if (postOpt.isPresent()) {
      Post post = postOpt.get();
      if (title != null)
        post.setTitle(title);
      if (content != null)
        post.setContent(content);
      postRepository.save(post);
      return post;
    }
    throw new NotFoundException();
  }

  public boolean deletePost(Long id) {
    try {
      postRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
