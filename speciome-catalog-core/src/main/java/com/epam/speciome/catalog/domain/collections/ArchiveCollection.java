package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;

public class ArchiveCollection {
    private final CollectionStorage collectionStorage;

    public ArchiveCollection(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public Collection archiveCollection(long id) {
        try {
            CollectionData collectionById = collectionStorage.getCollectionById(id);
            CollectionData collectionArchived = new CollectionData(
                    collectionById.collectionName(),
                    collectionById.createdAt(),
                    collectionById.updatedAt(),
                    collectionById.ownerEmail(),
                    true);
            collectionStorage.updateCollection(id, collectionArchived);
            return Collection.fromCollectionData(id, collectionArchived);
        } catch (CollectionIsNullException e) {
            throw new CollectionNotFoundException(id, e);
        }
    }
}
