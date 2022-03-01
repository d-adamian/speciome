package com.epam.speciome.catalog;

import com.epam.speciome.catalog.domain.collections.*;
import com.epam.speciome.catalog.domain.samples.*;
import com.epam.speciome.catalog.domain.users.GetUserDetails;
import com.epam.speciome.catalog.domain.users.GetUserPassword;
import com.epam.speciome.catalog.domain.users.RegisterUser;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import com.epam.speciome.catalog.persistence.api.users.UserStorage;

public class UseCaseFactory {
    private final UserStorage userStorage;
    private final SampleStorage sampleStorage;
    private final CollectionStorage collectionStorage;

    public UseCaseFactory(UserStorage userStorage, SampleStorage sampleStorage, CollectionStorage collectionStorage) {
        this.userStorage = userStorage;
        this.sampleStorage = sampleStorage;
        this.collectionStorage = collectionStorage;
    }

    public RegisterUser registerUser() {
        return new RegisterUser(userStorage);
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

    public ImportSamples importSamples() {
        return new ImportSamples(sampleStorage);
    }

    public ArchiveSample archiveSample() {
        return new ArchiveSample(sampleStorage);
    }

    public UnArchiveSample unArchiveSample() {
        return new UnArchiveSample(sampleStorage);
    }

    public AddCollection addCollection() {
        return new AddCollection(collectionStorage);
    }

    public GetCollection getCollection() {
        return new GetCollection(collectionStorage);
    }

    public ListCollections listCollections() {
        return new ListCollections(collectionStorage);
    }

    public ArchiveCollection archiveCollection() {
        return new ArchiveCollection(collectionStorage);
    }

    public UnArchiveCollection unArchiveCollection() {
        return new UnArchiveCollection(collectionStorage);
    }

    public RemoveCollection removeCollection() {
        return new RemoveCollection(collectionStorage);
    }

}
