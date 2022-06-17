package com.graphqlexample.project.services.implementations;

import com.graphqlexample.project.AbstractTestcontainers;
import com.graphqlexample.project.exceptions.ResourceNotFoundException;
import com.graphqlexample.project.models.entities.Role;
import com.graphqlexample.project.models.entities.User;
import com.graphqlexample.project.repositories.RoleRepository;
import com.graphqlexample.project.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop"
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest extends AbstractTestcontainers {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    User noRoleUser;

    User savedNoRoleUser;

    @BeforeEach
    public void setUp() {
        noRoleUser = new User("J Cole", "pass");
        savedNoRoleUser = userService.createUser(noRoleUser);

    }

    @Test
    void createUser_usingLoginAndPassword_createsUserwithCorrectUsernameAndPassword_withCorrectSetOfRoles() {
        var newUser = new User("user", "pass");
        var user = userService.createUser(newUser);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(user.getRoles().stream().map(Role::getName).collect(Collectors.toList())).contains(
                new String[]{"WRITE_USER", "READ_USER"});
    }

    @Test
    void createAdminUser_usingLoginAndPassword_createsUserWithAdminSetRoles_andCorrectUsernameAndPassword() {
        var newAdmin = new User("admin", "pass");
        var user = userService.createAdminUser(newAdmin);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(newAdmin.getUsername());
        assertThat(user.getPassword()).isEqualTo(newAdmin.getPassword());
        assertThat(user.getRoles().stream().map(Role::getName).collect(Collectors.toList())).contains(
                new String[]{"WRITE_USER", "READ_USER", "WRITE_ADMIN", "READ_ADMIN"});
    }

    @Test
    void findByUsername_usingExistingUsername_userIsEqualToExpectedOutput() {
        var user = userService.findByUsername(savedNoRoleUser.getUsername());
        log.info("This is user \"{}\"", user);
        log.info("This is noRoleUser \"{}\"", savedNoRoleUser);
        assertNotNull(user);
        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedNoRoleUser);
    }

    @Test
    void findByLoginAndPassword_tryingToLogInWithCorrectPassword_findsAndReturnsCorrectUser() {
        var user = userService.findByLoginAndPassword(noRoleUser.getUsername(), "pass");
        assertNotNull(user);
        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("id", "roles")
                .isEqualTo(savedNoRoleUser);
    }

    @Test
    void findByLoginPassword_tryingToLogInWithIncorrectPassword_throwsException() {
        AccessDeniedException thrownEx = assertThrows(AccessDeniedException.class, () -> {
            userService.findByLoginAndPassword(noRoleUser.getUsername(), "IncorrectPasswordEdition123");
        }, "Invalid user password");
        assertThat(thrownEx.getMessage()).isEqualTo("Invalid user password");
    }

    @Test
    void loadUserByUsername_usingCreatedUsernameThatExists_doesntThrowError() {
        assertDoesNotThrow(() -> userService.loadUserByUsername(noRoleUser.getUsername()));
    }
}