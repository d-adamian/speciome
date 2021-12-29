package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.AbsentCollectionNameException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;

public class AddCollection {
    private final CollectionStorage collectionStorage;

    public AddCollection(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public long addCollection(String collectionName) {
        if (collectionName == null) {
            throw new AbsentCollectionNameException();
        }
        long collectionId = collectionStorage.addCollection(new CollectionData(collectionName));
        return collectionId;
    }
}
