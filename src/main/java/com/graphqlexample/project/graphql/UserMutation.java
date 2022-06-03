package com.graphqlexample.project.graphql;

import lombok.RequiredArgsConstructor;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import com.graphqlexample.project.models.User;
import com.graphqlexample.project.services.UserService;
import com.graphqlexample.project.dtos.AuthResponseDto;
import com.graphqlexample.project.dtos.RegistrationDto;
import com.graphqlexample.project.config.jwt.JwtProvider;

@DgsComponent
@RequiredArgsConstructor
public class UserMutation {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @DgsMutation
    public AuthResponseDto login(@InputArgument RegistrationDto input) {
        User user = userService.findByLoginAndPassword(input.getUsername(), input.getPassword());
        String token = jwtProvider.generateToken(user.getUsername());
        return new AuthResponseDto(token);
    }

    @DgsMutation
    public String register(@InputArgument RegistrationDto input) {
        var user = new User();
        user.setUsername(input.getUsername());
        user.setPassword(input.getPassword());
        userService.createUser(user);
        return "OK";
    }
}
