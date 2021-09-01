package com.epam.specimenbase.catalog.persistence.testmocks;

import com.epam.specimenbase.catalog.persistence.api.users.UserStorage;
import com.epam.specimenbase.catalog.persistence.api.users.UserStorageContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Test reference mock user storage implementation")
public class TestInMemoryMapUserStorage implements UserStorageContract {
    private UserStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryMapUserStorage();
    }

    @Override
    public UserStorage userStorage() {
        return userStorage;
    }
}
