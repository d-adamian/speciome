package com.epam.specimenbase.catalog;

import com.epam.specimenbase.catalog.domain.samples.*;
import com.epam.specimenbase.catalog.domain.users.GetUserDetails;
import com.epam.specimenbase.catalog.domain.users.GetUserPassword;
import com.epam.specimenbase.catalog.domain.users.LogInUser;
import com.epam.specimenbase.catalog.domain.users.RegisterUser;
import com.epam.specimenbase.catalog.persistence.api.samples.SampleStorage;
import com.epam.specimenbase.catalog.persistence.api.users.UserStorage;

public class UseCaseFactory {
    private final UserStorage userStorage;
    private final SampleStorage sampleStorage;

    public UseCaseFactory(UserStorage userStorage, SampleStorage sampleStorage) {
        this.userStorage = userStorage;
        this.sampleStorage = sampleStorage;
    }

    public RegisterUser registerUser() {
        return new RegisterUser(userStorage);
    }

    public LogInUser logInUser() {
        return new LogInUser(userStorage);
    }

    public GetUserDetails getUserDetails() {
        return new GetUserDetails(userStorage);
    }

    public GetUserPassword getUserPassword() {
        return new GetUserPassword(userStorage);
    }

    public ListSamples listSamples() {
        return new ListSamples(sampleStorage);
    }

    public AddSample addSample() {
        return new AddSample(sampleStorage);
    }

    public GetSample getSample() {
        return new GetSample(sampleStorage);
    }

    public DeleteSample deleteSample() {
        return new DeleteSample(sampleStorage);
    }

    public UpdateSample updateSample() {
        return new UpdateSample(sampleStorage);
    }
}
