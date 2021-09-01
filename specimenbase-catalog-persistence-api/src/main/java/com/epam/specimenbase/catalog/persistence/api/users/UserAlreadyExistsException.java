package com.epam.specimenbase.catalog.persistence.api.users;

public final class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email) {
        super("User already exists: " + email);
    }
}
