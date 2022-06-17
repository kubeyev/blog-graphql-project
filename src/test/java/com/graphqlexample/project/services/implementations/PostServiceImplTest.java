package com.graphqlexample.project.services.implementations;

import com.graphqlexample.project.AbstractTestcontainers;
import com.graphqlexample.project.models.entities.Role;
import com.graphqlexample.project.models.entities.User;
import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.graphqlexample.project.models.entities.Post;
import com.graphqlexample.project.models.dtos.PostCreateDto;
import com.graphqlexample.project.models.dtos.PostUpdateDto;
import com.graphqlexample.project.repositories.PostRepository;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop"
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostServiceImplTest extends AbstractTestcontainers {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostServiceImpl postService;

    List<Post> posts;
    List<Post> expectedAllPosts;
    Post expectedOneGetPost;
    User adminUser;

    @BeforeEach
    public void setUp() {
        adminUser = userService.createAdminUser(new User("admin", "pass"));
        roleRepository.saveAll(List.of(
                new Role("READ_USER"),
                new Role("READ_ADMIN"),
                new Role("WRITE_USER"),
                new Role("WRITE_ADMIN")
        ));
        posts = List.of(
                new Post("first","1", LocalDate.parse("2022-01-01"), adminUser),
                new Post("second","2",LocalDate.parse("2022-02-02"), adminUser),
                new Post("third","3", LocalDate.parse("2022-03-03"), adminUser),
                new Post("forth","4", LocalDate.parse("2022-04-04"), adminUser),
                new Post("fifth","5", LocalDate.parse("2022-05-05"), adminUser),
                new Post("sixth","6", LocalDate.parse("2022-06-06"), adminUser)
        );
        expectedAllPosts = postRepository.saveAll(posts);
        expectedOneGetPost = expectedAllPosts.get(0);
    }

    @AfterEach
    public void clearing() {
        postRepository.deleteAll();
        postRepository.flush();
        roleRepository.deleteAll();
        roleRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
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
        var result = postService.getPost(expectedOneGetPost.getId());
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("user", "id")
                .isEqualTo(expectedOneGetPost);
    }

    @Test
    void gettingOnePost_usingUsingNotExistingID_thatShouldReturnException() {
        ResourceNotFoundException thrownEx = assertThrows(ResourceNotFoundException.class, () ->
                postService.getPost(100L),
                "Post with id 100 not found!");
        assertThat(thrownEx.getMessage()).isEqualTo("Post with id 100 not found!");
    }

    @Test
    void givenIDAndInput_returnPartiallyUpdatedPost() {
        var publishedDate = "2022-06-06";
        var toUpdatePostID = expectedOneGetPost.getId();
        var updatedTitle = "Testing update title";
        var updatedContent = "Testing update content";
        var updateDto = new PostUpdateDto(toUpdatePostID, updatedTitle, updatedContent, publishedDate);
        var initialPost = expectedOneGetPost;

        var expected = new Post(updatedTitle, updatedContent, LocalDate.parse(publishedDate), adminUser);
        var result = postService.updatePost(updateDto);
        assertThat(result).isNotNull();
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "user")
                .isEqualTo(expected)
                .isNotEqualTo(initialPost);
    }

    @Test
    void deletePost_withCorrectID_ifSuccessful_NoExceptionThrown() {
        Long postID = expectedOneGetPost.getId();

        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.context.SecurityContext securityContext = mock(
                org.springframework.security.core.context.SecurityContext.class
        );
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(adminUser);

        postService.deletePost(postID);
        ResourceNotFoundException thrownEx = assertThrows(ResourceNotFoundException.class, () ->
            postService.getPost(postID),
            "Post with id " + postID + " not found!");
        assertThat(thrownEx.getMessage()).isEqualTo("Post with id " + postID + " not found!");
    }

    @Test
    void deletePost_withWrongID_ExceptionShouldBeThrown() {
        Long postID = 100L;

        ResourceNotFoundException thrownEx = assertThrows(ResourceNotFoundException.class, () ->
                postService.deletePost(postID),
                "Post with id 100 not found!");
        assertThat(thrownEx.getMessage()).isEqualTo("Post with id 100 not found!");
    }
}