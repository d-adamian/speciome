package com.epam.speciome.catalog.domain.users;

import com.epam.speciome.catalog.persistence.api.exceptions.UserIsNullException;
import com.epam.speciome.catalog.persistence.api.users.UserStorage;

public final class GetUserPassword {
    private final UserStorage userStorage;

    public GetUserPassword(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String getUserPassword(String email) {
        try {
            return userStorage.loadUserData(email).passwordHash();
        } catch (UserIsNullException e) {
            throw new UserNotFoundException(email, e);
        }
    }

    public static final class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String email, Throwable e) {
            super("User not found for email: " + email, e);
        }
    }
}
