package com.epam.specimenbase.catalog.apiservice;

import com.epam.specimenbase.catalog.ports.UserData;
import com.epam.specimenbase.catalog.ports.UserStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryMapUserStorage implements UserStorage {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public void addUser(String email, String password) {
        userDataMap.put(email, new UserData(email, password));
    }

    @Override
    public Optional<UserData> loadUserData(String email) {
        return Optional.ofNullable(userDataMap.get(email));
    }
}
