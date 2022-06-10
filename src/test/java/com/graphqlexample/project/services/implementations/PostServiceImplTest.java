package com.graphqlexample.project.services.implementations;

import com.graphqlexample.project.AbstractTestcontainers;
import com.graphqlexample.project.models.entities.Role;
import com.graphqlexample.project.models.entities.User;
import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.graphqlexample.project.models.entities.Post;
import com.graphqlexample.project.models.dtos.PostCreateDto;
import com.graphqlexample.project.models.dtos.PostUpdateDto;
import com.graphqlexample.project.repositories.PostRepository;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop"
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostServiceImplTest extends AbstractTestcontainers {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostServiceImpl postService;

    List<Role> roles;
    List<Post> posts;
    List<Post> expectedAllPosts;
    Post expectedOneGetPost;
    User commentUser;
    User adminUser;

    @BeforeEach
    public void setUp() {
        commentUser = userRepository.save(new User("abl", "pass"));
        adminUser = userRepository.save(new User("admin", "pass"));
        roles = List.of(
                new Role(1L, "WRITE_USER"),
                new Role(2L, "WRITE_ADMIN"),
                new Role(3L, "READ_USER"),
                new Role(4L, "READ_ADMIN")
        );
        posts = List.of(
                new Post("first","1", "2022-01-01", adminUser),
                new Post("second","2","2022-02-02", adminUser),
                new Post("third","3", "2022-03-03", adminUser),
                new Post("forth","4", "2022-04-04", adminUser),
                new Post("fifth","5", "2022-05-05", adminUser),
                new Post("sixth","6", "2022-06-06", adminUser)
        );
        roleRepository.saveAll(roles);
        expectedOneGetPost = postRepository.save(new Post("seventh","7", "2022-07-07", adminUser));
        expectedAllPosts = postRepository.saveAll(posts);
    }

    @Test
    @DisplayName("This test case should successfully create post using correctly created PostCreateDto and return created object")
    void createPost_with_correctDto_andReturnCreatedUser() {
        var title = "Testing title";
        var content = "Testing Content";
        var publishedDate = "2022-06-06";
        var createDto = new PostCreateDto(title, content, publishedDate);
        var returnedPost = new Post(title, content, LocalDate.parse(publishedDate), adminUser);


        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.context.SecurityContext securityContext = mock(
                org.springframework.security.core.context.SecurityContext.class
        );
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(adminUser);

        var result = postService.createPost(createDto);
        assertThat(result).isNotNull();
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(returnedPost);
    }

    @Test
    void gettingAllPosts_FromDB_Successfully_usingInitiatedValueOfExpectedPosts() {
        var result = postService.getAllPosts();
        assertThat(result.size()).isEqualTo(expectedAllPosts.size());
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("user", "id")
                .isEqualTo(expectedAllPosts);
    }

//            result.forEach(post -> System.out.println(post));
    @Test
    void gettingOnePost_usingCorrectID() {
        var result = postService.getPost(1L);
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("user", "id")
                .isEqualTo(expectedOneGetPost);
    }

    @Test
    void gettingOnePost_usingUsingNotExistingID_thatShouldReturnException() {
        ResourceNotFoundException thrownEx = assertThrows(ResourceNotFoundException.class, () -> {
            postService.getPost(100L);
        }, "Post with id 100 not found!");
        assertThat(thrownEx.getMessage()).isEqualTo("Post with id 100 not found!");
    }

    @Test
    void givenIDAndInput_returnPartiallyUpdatedPost() {
        var publishedDate = "2022-06-06";
        var toUpdatePostID = 1L;
        var updatedTitle = "Testing update title";
        var updatedContent = "Testing update content";
        var updateDto = new PostUpdateDto(toUpdatePostID, updatedTitle, updatedContent, publishedDate);
        var initialPost = postRepository.findById(toUpdatePostID);

        var expected = new Post(updatedTitle, updatedContent, LocalDate.parse(publishedDate), adminUser);
        var result = postService.updatePost(updateDto);
        assertThat(result.getId()).isNotNull();
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "user")
                .isEqualTo(expected);
    }

    @Test
    void deletePost_withCorrectID() {
        Long postID = 1L;

        var result = postService.deletePost(postID);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void deletePost_withWrongID() {
        Long postID = 1L;
        Mockito.when(postService.deletePost(1L)).thenReturn(false);

        var result = postService.deletePost(postID);
        assertThat(result).isEqualTo(false);
    }
}