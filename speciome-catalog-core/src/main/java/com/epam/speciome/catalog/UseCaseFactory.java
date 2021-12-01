package com.epam.speciome.catalog;

import com.epam.speciome.catalog.domain.samples.*;
import com.epam.speciome.catalog.domain.users.GetUserDetails;
import com.epam.speciome.catalog.domain.users.GetUserPassword;
import com.epam.speciome.catalog.domain.users.LogInUser;
import com.epam.speciome.catalog.domain.users.RegisterUser;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import com.epam.speciome.catalog.persistence.api.users.UserStorage;

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

    public ExportSamples exportSamples() {
        return new ExportSamples(sampleStorage);
    }

    public ArchiveSample archiveSample() {
        return new ArchiveSample(sampleStorage);
    }

    public UnArchiveSample unArchiveSample() {
        return new UnArchiveSample(sampleStorage);
    }
}
