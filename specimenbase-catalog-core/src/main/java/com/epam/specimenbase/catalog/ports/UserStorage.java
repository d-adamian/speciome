package com.epam.specimenbase.catalog.ports;

import java.util.Optional;

public interface UserStorage {

    // TODO: change password to salted hash
    void addUser(String email, String password);

    Optional<UserData> loadUserData(String email);
}
