package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.users.UserAlreadyExistsException;
import com.epam.speciome.catalog.persistence.api.users.UserData;
import com.epam.speciome.catalog.persistence.api.users.UserStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryMapUserStorage implements UserStorage {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public void addUser(UserData userData) {
        String email = userData.email();
        if (userDataMap.containsKey(email)) {
            throw new UserAlreadyExistsException(email);
        }
        userDataMap.put(email, userData);
    }

    @Override
    public Optional<UserData> loadUserData(String email) {
        return Optional.ofNullable(userDataMap.get(email));
    }
}
