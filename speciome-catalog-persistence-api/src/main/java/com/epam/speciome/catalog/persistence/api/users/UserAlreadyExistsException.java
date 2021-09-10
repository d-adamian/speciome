package com.epam.speciome.catalog.persistence.api.users;

public final class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email) {
        super("User already exists: " + email);
    }
}
