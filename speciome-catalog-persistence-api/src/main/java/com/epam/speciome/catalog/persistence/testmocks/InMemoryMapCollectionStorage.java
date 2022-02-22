package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;

import java.util.HashMap;
import java.util.Map;

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
    public CollectionData getCollectionById(long collectionId) {
        CollectionData retrievedCollection = collectionDataMap.get(collectionId);
        if (retrievedCollection == null) {
            throw new CollectionIsNullException(collectionId);
        }
        return retrievedCollection;
    }

    @Override
    public void updateCollection(Long collectionId, CollectionData collectionData) {
        collectionDataMap.put(collectionId, collectionData);
    }

    public void clear() {
        collectionDataMap.clear();
        maxCollectionId = 1L;
    }
}
