package com.epam.speciome.catalog.domain.users;

import com.epam.speciome.catalog.persistence.api.users.UserData;
import com.epam.speciome.catalog.persistence.api.users.UserStorage;

public final class GetUserPassword {
    private final UserStorage userStorage;

    public GetUserPassword(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String getUserPassword(String email) {
        return userStorage.loadUserData(email).map(UserData::getPasswordHash)
                .orElseThrow(UserNotFoundException::new);
    }

    public static final class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super("User not found");
        }
    }
}
