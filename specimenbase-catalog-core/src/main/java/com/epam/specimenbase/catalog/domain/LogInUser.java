package com.epam.specimenbase.catalog.domain;

import com.epam.specimenbase.catalog.ports.UserStorage;

import java.util.Objects;

public final class LogInUser {
    private final UserStorage userStorage;

    public LogInUser(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User logIn(String email, String password) {
        return userStorage.loadUserData(email)
                .filter(userData -> Objects.equals(password, userData.getPassword()))
                .map(userData -> new User(userData.getEmail()))
                .orElseThrow(InvalidCredentialsException::new);
    }

}
