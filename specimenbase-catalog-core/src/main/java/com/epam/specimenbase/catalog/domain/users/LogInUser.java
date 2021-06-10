package com.epam.specimenbase.catalog.domain.users;

import com.epam.specimenbase.catalog.ports.UserData;
import com.epam.specimenbase.catalog.ports.UserStorage;

public final class LogInUser {
    private final UserStorage userStorage;

    public LogInUser(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User logIn(String email, String password) {
        return userStorage.loadUserData(email)
                .filter(userData -> passwordMatches(password, userData))
                .map(userData -> new User(userData.getEmail()))
                .orElseThrow(InvalidCredentialsException::new);
    }

    private static boolean passwordMatches(String password, UserData userData) {
        String passwordHash = Passwords.hashPassword(userData.getSalt(), password);
        return userData.getPasswordHash().equals(passwordHash);
    }

}
