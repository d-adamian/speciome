package com.epam.specimenbase.catalog.apiservice.endpoints;

import com.epam.specimenbase.catalog.domain.InvalidCredentialsException;
import com.epam.specimenbase.catalog.domain.User;
import com.epam.specimenbase.catalog.ports.UseCaseFactory;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.apache.http.HttpStatus;

public final class UserController {
    private static final String USER_SESSION_ATTRIBUTE = "user";

    private final UseCaseFactory useCaseFactory;

    public UserController(UseCaseFactory useCaseFactory) {
        this.useCaseFactory = useCaseFactory;
    }

    @OpenApi(
            path = "/register-user",
            method = HttpMethod.POST,
            description = "Register new user. Does not start session, log in is needed.",
            summary = "Register new user",
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = RegisterUserRequest.class)),
            responses = {
                    @OpenApiResponse(status = "201") // TODO: handle duplicate registration case
            }
    )
    public void registerUser(Context ctx) {
        RegisterUserRequest request = ctx.bodyAsClass(RegisterUserRequest.class);
        useCaseFactory.registerUser().registerNewUser(request.getEmail(), request.getPassword());
        ctx.status(HttpStatus.SC_CREATED);
    }

    @OpenApi(
            path = "/login",
            method = HttpMethod.POST,
            description = "Log in",
            summary = "Log in",
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = LogInRequest.class)),
            responses = {
                    @OpenApiResponse(status = "200"),
                    @OpenApiResponse(status = "401", description = "Invalid credentials")
            }
    )
    public void logInUser(Context ctx) {
        LogInRequest request = ctx.bodyAsClass(LogInRequest.class);
        try {
            User user = useCaseFactory.logInUser().logIn(request.getEmail(), request.getPassword());
            ctx.sessionAttribute(USER_SESSION_ATTRIBUTE, user);
            ctx.status(HttpStatus.SC_OK);
        } catch (InvalidCredentialsException e) {
            ctx.status(HttpStatus.SC_UNAUTHORIZED);
        }
    }

    @OpenApi(
            path = "/logout",
            method = HttpMethod.POST,
            description = "Log out",
            summary = "Log out",
            responses = {@OpenApiResponse(status = "200")}
    )
    public void logOut(Context ctx) {
        ctx.sessionAttribute(USER_SESSION_ATTRIBUTE, null);
        ctx.status(HttpStatus.SC_OK);
    }

    @OpenApi(
            path = "/user-details",
            method = HttpMethod.GET,
            description = "Retrieves details of the current user",
            summary = "Get user details",
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "401", description = "Unauthorized")
            }
    )
    public void getUserDetails(Context ctx) {
        User user = ctx.sessionAttribute(USER_SESSION_ATTRIBUTE);
        var response = useCaseFactory.getUserDetails().getUserDetails(user);
        ctx.json(response.getEmail());
    }

    public static final class RegisterUserRequest {
        private final String email;
        private final String password;

        @JsonCreator
        public RegisterUserRequest(
                @JsonProperty("email") String email,
                @JsonProperty("password") String password
        ) {
            this.email = email;
            this.password = password;
        }

        @JsonProperty
        public String getEmail() {
            return email;
        }

        @JsonProperty
        public String getPassword() {
            return password;
        }
    }

    public static final class LogInRequest {
        private final String email;
        private final String password;

        @JsonCreator
        public LogInRequest(
                @JsonProperty("email") String email,
                @JsonProperty("password") String password
        ) {
            this.email = email;
            this.password = password;
        }

        @JsonProperty
        public String getEmail() {
            return email;
        }

        @JsonProperty
        public String getPassword() {
            return password;
        }
    }
}
