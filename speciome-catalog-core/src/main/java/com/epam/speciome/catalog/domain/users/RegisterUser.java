package com.epam.speciome.catalog.domain.users;

import com.epam.speciome.catalog.persistence.api.exceptions.UserIsNullException;
import com.epam.speciome.catalog.persistence.api.users.UserData;
import com.epam.speciome.catalog.persistence.api.users.UserStorage;
import org.apache.commons.validator.routines.EmailValidator;

public final class RegisterUser {
    private final UserStorage userStorage;

    public RegisterUser(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void registerNewUser(String email, String password) {
        checkEmail(email);
        if(doesUserExist(email)) {
            throw new UserAlreadyExistsException();
        }
        UserData userData = new UserData(email, password);
        userStorage.addUser(userData);
    }

    private void checkEmail(String email) {
        boolean valid = EmailValidator.getInstance().isValid(email);
        if (!valid) {
            throw new EmailInvalidException();
        }
    }

    private boolean doesUserExist(String email) {
        try {
            userStorage.loadUserData(email);
            return true;
        } catch (UserIsNullException e) {
            return false;
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
