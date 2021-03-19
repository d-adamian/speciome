package com.epam.specimenbase.catalog.ports;

public final class UserData {
    private final String email;
    private final String salt;
    private final String passwordHash;

    public UserData(String email, String salt, String passwordHash) {
        this.email = email;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getSalt() {
        return salt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
