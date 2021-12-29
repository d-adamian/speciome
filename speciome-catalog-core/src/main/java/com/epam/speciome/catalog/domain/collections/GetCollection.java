package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;

import java.util.Optional;

public class GetCollection {
    private final CollectionStorage collectionStorage;

    public GetCollection(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public Collection getCollection(long collectionId) {
        Optional<CollectionData> collectionDataOptional = collectionStorage.getCollectionById(collectionId);
        return collectionDataOptional
                .map(collectionData -> new Collection(collectionId, collectionData))
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
    }
}
