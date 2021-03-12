package com.epam.specimenbase.catalog.domain;

import com.epam.specimenbase.catalog.ports.UserData;
import com.epam.specimenbase.catalog.ports.UserStorage;

public final class RegisterUser {
    private final UserStorage userStorage;

    public RegisterUser(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void registerNewUser(String email, String password) {
        String salt = Passwords.generateSalt();
        String passwordHash = Passwords.hashPassword(salt, password);
        UserData userData = new UserData(email, salt, passwordHash);
        userStorage.addUser(userData);
    }
}
