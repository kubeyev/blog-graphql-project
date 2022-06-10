package com.graphqlexample.project.repositories;

import com.graphqlexample.project.models.entities.User;
import com.graphqlexample.project.models.entities.Post;
import com.graphqlexample.project.models.entities.Role;
import com.graphqlexample.project.AbstractTestcontainers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop"
)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PostRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    User commentUser;

    @BeforeEach
    public void setUp() {
        commentUser = userRepository.save(new User("abl", "pass"));
    }


    @Test
    void givenPost_savePostSuccessfully() {
        Post expectedResult = new Post("A$AP ROCKY TITLE",
                "A$AP ROCKY CONTENT",
                LocalDate.parse("2002-06-06"),
                commentUser);

        Post actualResult = postRepository.save(expectedResult);
        assertThat(actualResult).usingRecursiveComparison()
                .ignoringFields("ID").isEqualTo(expectedResult);
    }
}