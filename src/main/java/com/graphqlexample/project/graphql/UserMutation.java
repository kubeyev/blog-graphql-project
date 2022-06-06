package com.graphqlexample.project.graphql;

import lombok.RequiredArgsConstructor;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import com.graphqlexample.project.models.entities.User;
import com.graphqlexample.project.services.implementations.UserServiceImpl;
import com.graphqlexample.project.models.response.AuthResponseDto;
import com.graphqlexample.project.models.dtos.RegistrationDto;
import com.graphqlexample.project.config.jwt.JwtProvider;

@DgsComponent
@RequiredArgsConstructor
public class UserMutation {

    private final UserServiceImpl userServiceImpl;
    private final JwtProvider jwtProvider;

    @DgsMutation
    public AuthResponseDto login(@InputArgument RegistrationDto input) {
        User user = userServiceImpl.findByLoginAndPassword(input.getUsername(), input.getPassword());
        String token = jwtProvider.generateToken(user.getUsername());
        return new AuthResponseDto(token);
    }

    @DgsMutation
    public String register(@InputArgument RegistrationDto input) {
        var user = new User();
        user.setUsername(input.getUsername());
        user.setPassword(input.getPassword());
        userServiceImpl.createUser(user);
        return "OK";
    }
}
