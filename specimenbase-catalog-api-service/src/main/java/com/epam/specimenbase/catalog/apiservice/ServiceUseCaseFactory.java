package com.epam.specimenbase.catalog.apiservice;

import com.epam.specimenbase.catalog.domain.samples.*;
import com.epam.specimenbase.catalog.domain.users.GetUserDetails;
import com.epam.specimenbase.catalog.domain.users.LogInUser;
import com.epam.specimenbase.catalog.domain.users.RegisterUser;
import com.epam.specimenbase.catalog.ports.SampleStorage;
import com.epam.specimenbase.catalog.ports.UseCaseFactory;
import com.epam.specimenbase.catalog.ports.UserStorage;

public final class ServiceUseCaseFactory implements UseCaseFactory {
    private final UserStorage userStorage;
    private final SampleStorage sampleStorage;

    public ServiceUseCaseFactory() {
        this.userStorage = new InMemoryMapUserStorage();
        this.sampleStorage = new InMemoryMapSampleStorage();
    }

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

    @Override
    public ListSamples listSamples() {
        return new ListSamples(sampleStorage);
    }

    @Override
    public AddSample addSample() {
        return new AddSample(sampleStorage);
    }

    @Override
    public GetSample getSample() {
        return new GetSample(sampleStorage);
    }

    @Override
    public DeleteSample deleteSample() {
        return new DeleteSample(sampleStorage);
    }

    @Override
    public UpdateSample updateSample() {
        return new UpdateSample(sampleStorage);
    }
}
