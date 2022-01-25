package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryMapCollectionStorage implements CollectionStorage {
    private long maxCollectionId = 1L;
    private final Map<Long, CollectionData> collectionDataMap = new HashMap<>();

    @Override
    public long addCollection(CollectionData collectionData) {
        long collectionId = maxCollectionId;
        collectionDataMap.put(collectionId, collectionData);
        maxCollectionId++;
        return collectionId;
    }

    @Override
    public ListCollectionsResult listCollections() {
        return new ListCollectionsResult(collectionDataMap.size(), collectionDataMap);
    }

    @Override
    public Optional<CollectionData> getCollectionById(long collectionId) {
        return Optional.ofNullable(collectionDataMap.get(collectionId));
    }

    public void clear() {
        collectionDataMap.clear();
    }
}
