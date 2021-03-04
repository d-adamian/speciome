package com.epam.specimenbase.catalog.ports;

public final class UserData {
    private final String email;
    private final String password;

    public UserData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
