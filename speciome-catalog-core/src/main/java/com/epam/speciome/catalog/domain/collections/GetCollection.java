package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;

public class GetCollection {
    private final CollectionStorage collectionStorage;

    public GetCollection(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public Collection getCollection(long collectionId) {
        try {
            return new Collection(collectionId, collectionStorage.getCollectionById(collectionId));
        } catch (CollectionIsNullException e) {
            throw new CollectionNotFoundException(collectionId, e);
        }
    }
}
