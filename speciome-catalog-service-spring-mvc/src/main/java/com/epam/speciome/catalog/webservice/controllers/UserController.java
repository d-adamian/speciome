package com.epam.speciome.catalog.webservice.controllers;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.users.GetUserDetails;
import com.epam.speciome.catalog.domain.users.RegisterUser;
import com.epam.speciome.catalog.domain.users.User;
import com.epam.speciome.catalog.webservice.ApiConstants;
import com.epam.speciome.catalog.webservice.exceptions.InvalidInputException;
import com.epam.speciome.catalog.webservice.models.UserCredentials;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Users", description = "User management API")
@RestController
public class UserController {
    @Autowired
    private UseCaseFactory useCaseFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Get user details",
            description = "Returns details of the current user"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK", responseCode = "200"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401"
            )
    })
    @GetMapping(path = ApiConstants.USER_DETAILS)
    public @ResponseBody
    String getUserDetails(Principal principal) {
        String userEmail = principal.getName();
        GetUserDetails.Response result = useCaseFactory.getUserDetails().getUserDetails(new User(userEmail));
        return result.email();
    }

    @Operation(
            summary = "Register new user",
            description = "Register new user with given email and password"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Success", responseCode = "204"
            ),
            @ApiResponse(
                    description = "E-mail invalid or user already exists", responseCode = "400"
            )
    })
    @PostMapping(path = ApiConstants.NEW_USER)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerUser(@RequestBody UserCredentials credentials) {
        try {
            String hashedPassword = passwordEncoder.encode(credentials.getPassword());
            useCaseFactory.registerUser().registerNewUser(credentials.getEmail(), hashedPassword);
        } catch (RegisterUser.EmailInvalidException | RegisterUser.UserAlreadyExistsException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    // Method is defined only for OpenAPI documentation, it's overridden by Spring Security form login
    @Operation(
            summary = "Log in user",
            description = "Log in user by email and password"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Success", responseCode = "204"
            ),
            @ApiResponse(
                    description = "Credentials are invalid", responseCode = "401"
            )
    })
    @PostMapping(path = ApiConstants.LOGIN)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void login(
            @RequestParam("email") @Parameter(description = "User email", example = "user@company.com") String email,
            @RequestParam("password") @Parameter(description = "Password", example = "pa$sw0rd") String password
    ) {
        // Method body unused
    }
}
