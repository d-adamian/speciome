package com.epam.speciome.catalog.domain.users;

import com.epam.speciome.catalog.domain.exceptions.InvalidCredentialsException;
import com.epam.speciome.catalog.persistence.api.users.UserStorage;

public final class GetUserDetails {
    private final UserStorage userStorage;

    public GetUserDetails(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public record Response(String email) {
    }

    public Response getUserDetails(User user) {
        if (user == null) {
            throw new InvalidCredentialsException();
        } else {
            String email = user.email();
            return new Response(email);
        }
    }
}
