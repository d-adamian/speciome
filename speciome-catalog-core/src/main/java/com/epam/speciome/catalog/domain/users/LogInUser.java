package com.epam.speciome.catalog.domain.users;

import com.epam.speciome.catalog.persistence.api.users.UserStorage;

// TODO: remove after Javalin-Vue PoC implementation is removed
public final class LogInUser {
    private final UserStorage userStorage;

    public LogInUser(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User logIn(String email, String password) {
        return userStorage.loadUserData(email)
                .filter(userData -> userData.getPasswordHash().equals(password))
                .map(userData -> new User(userData.getEmail()))
                .orElseThrow(InvalidCredentialsException::new);
    }
}
