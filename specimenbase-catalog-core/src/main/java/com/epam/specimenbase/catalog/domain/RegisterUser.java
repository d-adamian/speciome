package com.epam.specimenbase.catalog.domain;

import com.epam.specimenbase.catalog.ports.UserStorage;

public final class RegisterUser {
    private final UserStorage userStorage;

    public RegisterUser(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void registerNewUser(String email, String password) {
        userStorage.addUser(email, password);
    }
}
