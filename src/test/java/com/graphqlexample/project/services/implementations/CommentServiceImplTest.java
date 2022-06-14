package com.graphqlexample.project.services.implementations;

import com.graphqlexample.project.AbstractTestcontainers;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;
import com.graphqlexample.project.models.dtos.CommentCreateDto;
import com.graphqlexample.project.models.dtos.CommentUpdateDto;
import com.graphqlexample.project.models.entities.Comment;
import com.graphqlexample.project.models.entities.Post;
import com.graphqlexample.project.models.entities.Role;
import com.graphqlexample.project.models.entities.User;
import com.graphqlexample.project.repositories.CommentRepository;
import com.graphqlexample.project.repositories.PostRepository;
import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.repositories.UserRepository;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop"
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentServiceImplTest  extends AbstractTestcontainers {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private UserServiceImpl userService;

    List<Role> roles;
    Post post;

    List<Comment> comments;
    Comment oneComment;
    List<Comment> expectedAllComments;
    User commentUser;
    User adminUser;

    @BeforeEach
    public void setUp() {
        roles = List.of(
                new Role("WRITE_USER"),
                new Role("WRITE_ADMIN"),
                new Role("READ_USER"),
                new Role("READ_ADMIN")
        );
        roles = roleRepository.saveAll(roles);
        commentUser = userService.createUser(new User("abl", "pass"));
        adminUser = userService.createAdminUser(new User("admin", "pass"));
        post = postRepository.save(new Post("first","1", "2022-01-01", adminUser));
        comments = List.of(
                new Comment("The post is perfect", LocalDate.parse("2022-02-07"), post, commentUser),
                new Comment("The post is damn good", LocalDate.parse("2022-02-08"), post, commentUser),
                new Comment("The post is questionable", LocalDate.parse("2022-02-09"), post, commentUser),
                new Comment("The post is the worst thing I ever read", LocalDate.parse("2022-02-10"), post, commentUser)
        );
        oneComment = comments.get(1);
        System.out.println(roleRepository.findAll());
        expectedAllComments = commentRepository.saveAll(comments);

//        Authentication authentication = mock(Authentication.class);
//        org.springframework.security.core.context.SecurityContext securityContext = mock(
//                org.springframework.security.core.context.SecurityContext.class
//        );
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(commentUser);
    }

    @AfterEach
    public void clearing() {
        commentRepository.deleteAll();
        commentRepository.flush();
        postRepository.deleteAll();
        postRepository.flush();
        roleRepository.deleteAll();
        roleRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    @DisplayName("This test case should successfully create post using correctly created PostCreateDto and return created object")
    void createComment_with_correctDto_andReturnCreatedComment() {
        var content = "Testing Content";
        var publishedDate = "2022-06-06";
        var createDto = new CommentCreateDto(content, publishedDate, post);
        var expected = new Comment(content, LocalDate.parse(publishedDate), post, adminUser);

        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.context.SecurityContext securityContext = mock(
                org.springframework.security.core.context.SecurityContext.class
        );
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(adminUser);

        var result = commentService.createComment(createDto);
        assertThat(result).isNotNull();
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void gettingAllComments_FromDB_Successfully_usingInitiatedValueOfExpectedComments() {
        var result = commentService.getAllComments();
        assertThat(result.size()).isEqualTo(expectedAllComments.size());
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("user", "id", "post")
                .isEqualTo(expectedAllComments);
    }

    @Test
    void gettingOneComment_usingCorrectID() {
        var expected = commentRepository.save(
                new Comment("This is jai comment", LocalDate.parse("2022-02-15"), post, commentUser)
        );
        System.out.println(expected.getId());
        var result = commentService.getComment(expected.getId());
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("user", "id", "post")
                .isEqualTo(expected);
    }

    @Test
    void gettingOneComment_usingUsingNotExistingID_thatShouldReturnException() {
        ResourceNotFoundException thrownEx = assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getComment(1000L);
        }, "Comment with id 100 not found!");
        assertThat(thrownEx.getMessage()).isEqualTo("Comment with id 100 not found!");
    }

    @Test
    void givenIDAndInput_returnPartiallyUpdatedComment() {
        var publishedDate = "2022-06-06";
        var toUpdateCommentID = oneComment.getId();
        var updatedContent = "Testing update content";
        var updateDto = new CommentUpdateDto(toUpdateCommentID, updatedContent, publishedDate);
        var initialPost = oneComment;

        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.context.SecurityContext securityContext = mock(
                org.springframework.security.core.context.SecurityContext.class
        );
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(commentUser);

        var expected = new Comment(updatedContent, LocalDate.parse(publishedDate), post, commentUser);
        var result = commentService.updateComment(updateDto);
        assertThat(result.getId()).isNotNull();
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "user", "post")
                .isEqualTo(expected)
                .isNotEqualTo(initialPost);
    }

    @Test
    void deleteComment_withCorrectID() {
        Long commentID = oneComment.getId();

        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.context.SecurityContext securityContext = mock(
                org.springframework.security.core.context.SecurityContext.class
        );
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(commentUser);

        commentService.deleteComment(commentID);
        ResourceNotFoundException thrownEx = assertThrows(ResourceNotFoundException.class, () ->{
            commentRepository.findById(commentID);
        }, "Comment with id 1 not found!");
        assertThat(thrownEx.getMessage()).isEqualTo("Comment with id 1 not found!");
    }

    @Test
    void deleteComment_withWrongID() {
        Long commentID = 100L;

        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.context.SecurityContext securityContext = mock(
                org.springframework.security.core.context.SecurityContext.class
        );
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(commentUser);

        ResourceNotFoundException thrownEx = assertThrows(ResourceNotFoundException.class, () -> {
            commentService.deleteComment(commentID);
        }, "Comment with id 100 not found!");
        assertThat(thrownEx.getMessage()).isEqualTo("Comment with id 100 not found!");
        ;
    }

}