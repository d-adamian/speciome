package com.epam.speciome.catalog.persistence.api.users;

public final class UserData {
    private final String email;
    private final String passwordHash;

    public UserData(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
