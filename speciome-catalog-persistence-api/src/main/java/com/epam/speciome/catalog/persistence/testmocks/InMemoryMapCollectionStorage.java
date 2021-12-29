package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryMapCollectionStorage implements CollectionStorage {
    private long maxCollectionId = 1l;
    private final Map<Long, CollectionData> collectionsById = new HashMap<>();

    @Override
    public long addCollection(CollectionData collectionData) {
        Long collectionId = maxCollectionId;
        collectionsById.put(collectionId, collectionData);
        maxCollectionId++;
        return collectionId;
    }

    @Override
    public Optional<CollectionData> getCollectionById(long collectionId) {
        return Optional.ofNullable(collectionsById.get(collectionId));
    }

    public void clear() {
        collectionsById.clear();
    }
}
