package com.graphqlexample.project.services.services;

import com.graphqlexample.project.models.entities.Post;
import com.graphqlexample.project.models.dtos.PostCreateDto;
import com.graphqlexample.project.models.dtos.PostUpdateDto;
import org.springframework.security.access.AccessDeniedException;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;


import java.util.List;

public interface PostService {
    public List<Post> getAllPosts();

    public List<Post> getPostsByCount(final int count);

    public Post getPost(final Long id);

    public int countPosts();

    public Post createPost(PostCreateDto input);

    public Post updatePost(PostUpdateDto input) throws AccessDeniedException, ResourceNotFoundException;

    public boolean deletePost(Long id);
}
