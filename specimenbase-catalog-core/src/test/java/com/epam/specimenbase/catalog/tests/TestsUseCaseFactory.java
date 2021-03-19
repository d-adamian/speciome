package com.epam.specimenbase.catalog.tests;

import com.epam.specimenbase.catalog.domain.GetUserDetails;
import com.epam.specimenbase.catalog.domain.LogInUser;
import com.epam.specimenbase.catalog.domain.RegisterUser;
import com.epam.specimenbase.catalog.ports.UseCaseFactory;
import com.epam.specimenbase.catalog.ports.UserStorage;

public final class TestsUseCaseFactory implements UseCaseFactory {
    private final UserStorage userStorage = new InMemoryMapUserStorage();

    @Override
    public RegisterUser registerUser() {
        return new RegisterUser(userStorage);
    }

    @Override
    public LogInUser logInUser() {
        return new LogInUser(userStorage);
    }

    @Override
    public GetUserDetails getUserDetails() {
        return new GetUserDetails(userStorage);
    }
}
