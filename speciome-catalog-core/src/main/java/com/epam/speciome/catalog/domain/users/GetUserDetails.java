package com.epam.speciome.catalog.domain.users;

import com.epam.speciome.catalog.persistence.api.users.UserStorage;

public final class GetUserDetails {
    private final UserStorage userStorage;

    public GetUserDetails(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public static final class Response {
        private final String email;

        public Response(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }

    public Response getUserDetails(User user) {
        if (user == null) {
            throw new InvalidCredentialsException();
        } else {
            String email = user.getEmail();
            return new Response(email);
        }
    }
}
