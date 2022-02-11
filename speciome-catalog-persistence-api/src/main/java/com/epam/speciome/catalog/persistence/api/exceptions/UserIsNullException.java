package com.epam.speciome.catalog.persistence.api.exceptions;

public class UserIsNullException extends RuntimeException {
    public UserIsNullException(String email) {
        super("User not found for email: " + email);
    }
}
