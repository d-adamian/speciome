package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;

import java.util.*;
import java.util.stream.Collectors;

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
    public void updateCollection(Long collectionId, CollectionData collectionData) {
        collectionDataMap.put(collectionId, collectionData);
    }

    @Override
    public ListCollectionsResult listCollections() {
        return new ListCollectionsResult(collectionDataMap.size(), collectionDataMap);
    }

    //TODO
    @Override
    public ListCollectionsResult sortedListCollections(String sortBy, boolean isDecrease) {


        Comparator<Map.Entry<Long, CollectionData>> comparator;
        comparator = (e1, e2) -> isDecrease ?
                e2.getValue().collectionName().compareTo(e1.getValue().collectionName()) :
                e1.getValue().collectionName().compareTo(e2.getValue().collectionName());

        if (sortBy.equals("id")) {
            comparator = (e1, e2) -> isDecrease ?
                    e2.getKey().compareTo(e1.getKey()) :
                    e1.getKey().compareTo(e2.getKey());

        }
        if (sortBy.equals("createdAtUtc")) {
            comparator = (e1, e2) -> isDecrease ?
                    e2.getValue().createdAt().compareTo(e1.getValue().createdAt()) :
                    e1.getValue().createdAt().compareTo(e2.getValue().createdAt());

        }
        if (sortBy.equals("updatedAtUtc")) {
            comparator = (e1, e2) -> isDecrease ?
                    e2.getValue().updatedAt().compareTo(e1.getValue().updatedAt()) :
                    e1.getValue().updatedAt().compareTo(e2.getValue().updatedAt());

        }
        if (sortBy.equals("ownerEmail")) {
            comparator = (e1, e2) -> isDecrease ?
                    e2.getValue().ownerEmail().compareTo(e1.getValue().ownerEmail()):
                    e1.getValue().ownerEmail().compareTo(e2.getValue().ownerEmail());

        }
        Map<Long, CollectionData> resultMap = new LinkedHashMap<>();
        collectionDataMap.entrySet().stream().sorted(comparator).forEachOrdered(e -> resultMap.put(e.getKey(), e.getValue()));

        return new ListCollectionsResult(resultMap.size(), resultMap, new ArrayList<>(resultMap.keySet()));

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
    public void removeCollectionById(Long collectionId) {
        collectionDataMap.remove(collectionId);
    }

    public void clear() {
        collectionDataMap.clear();
        maxCollectionId = 1L;
    }
}
