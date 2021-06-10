package com.epam.specimenbase.catalog.domain.users;

public final class User {
    private final String email;

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
