package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.CollectionNotArchivedException;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;

public final class RemoveCollection {
    private final CollectionStorage collectionStorage;

    public RemoveCollection(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public void removeCollection(Long collectionId) {
        try {
            CollectionData collectionById = collectionStorage.getCollectionById(collectionId);
            if (collectionById.isArchived()) {
                collectionStorage.removeCollectionById(collectionId);
            } else {
                throw new CollectionNotArchivedException(collectionId);
            }
        } catch (CollectionIsNullException e) {
            throw new CollectionNotFoundException(collectionId, e);
        }
    }
}

