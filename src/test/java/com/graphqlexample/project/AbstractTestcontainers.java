package com.graphqlexample.project;

import com.graphqlexample.project.models.entities.Post;
import com.graphqlexample.project.models.entities.Role;
import com.graphqlexample.project.models.entities.User;
import com.graphqlexample.project.repositories.CommentRepository;
import com.graphqlexample.project.repositories.PostRepository;
import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
public abstract class AbstractTestcontainers {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.29")
            .withUrlParam("characterEncoding", "UTF-8")
            .withUrlParam("serverTimezone", "UTC")
            .withImagePullPolicy(PullPolicy.defaultPolicy());

    @DynamicPropertySource
    private static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }
}