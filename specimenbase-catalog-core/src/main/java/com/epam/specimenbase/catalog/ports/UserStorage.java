package com.epam.specimenbase.catalog.ports;

import java.util.Optional;

public interface UserStorage {

    void addUser(UserData userData);

    Optional<UserData> loadUserData(String email);
}
