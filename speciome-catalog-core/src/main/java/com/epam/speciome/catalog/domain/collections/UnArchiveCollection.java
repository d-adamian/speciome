package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;

public class UnArchiveCollection {
    private final CollectionStorage collectionStorage;

    public UnArchiveCollection(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public Collection unArchiveCollection(long id) {
        try {
            CollectionData collectionById = collectionStorage.getCollectionById(id);
            CollectionData collectionUnArchived = new CollectionData(
                    collectionById.collectionName(),
                    collectionById.createdAt(),
                    collectionById.updatedAt(),
                    collectionById.ownerEmail(),
                    false);
            collectionStorage.updateCollection(id, collectionUnArchived);
            return Collection.fromCollectionData(id, collectionUnArchived);
        } catch (CollectionIsNullException e) {
            throw new CollectionNotFoundException(id, e);
        }
    }
}
