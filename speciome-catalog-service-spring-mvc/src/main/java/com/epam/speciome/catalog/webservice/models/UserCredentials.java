package com.epam.speciome.catalog.webservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserCredentials {
    private final String email;
    private final String password;

    @JsonCreator
    public UserCredentials(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    @Schema(description = "User email", example = "user@company.com")
    @JsonProperty public String getEmail() {
        return email;
    }

    @Schema(description = "Password", example = "pa$sw0rd", minLength = 1)
    @JsonProperty public String getPassword() {
        return password;
    }
}
