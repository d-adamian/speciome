package com.epam.specimenbase.catalog.domain.users;

import com.epam.specimenbase.catalog.ports.UserData;
import com.epam.specimenbase.catalog.ports.UserStorage;
import org.apache.commons.validator.routines.EmailValidator;

public final class RegisterUser {
    private final UserStorage userStorage;

    public RegisterUser(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void registerNewUser(String email, String password) {
        checkEmail(email);
        userStorage.loadUserData(email).ifPresent(userData -> {
            throw new UserAlreadyExistsException();
        });

        String salt = Passwords.generateSalt();
        String passwordHash = Passwords.hashPassword(salt, password);
        UserData userData = new UserData(email, salt, passwordHash);
        userStorage.addUser(userData);
    }

    private void checkEmail(String email) {
        boolean valid = EmailValidator.getInstance().isValid(email);
        if (!valid) {
            throw new EmailInvalidException();
        }
    }

    public static final class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException() {
            super("User already exists, re-registration is not allowed");
        }
    }

    public static final class EmailInvalidException extends RuntimeException {
        public EmailInvalidException() {
            super("Can not register user with invalid e-mail address");
        }
    }
}
