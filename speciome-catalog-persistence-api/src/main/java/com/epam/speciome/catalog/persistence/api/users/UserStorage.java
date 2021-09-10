package com.epam.speciome.catalog.persistence.api.users;

import java.util.Optional;

public interface UserStorage {

    void addUser(UserData userData);

    Optional<UserData> loadUserData(String email);
}
