package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

    @Override
    public ListCollectionsResult sortedListCollections(String sortBy, boolean isDescent) {

        Map<String, String> fieldMap = Map.of(
                "createdAtUtc", "createdAt",
                "updatedAtUtc", "createdAt"
        );

        sortBy = fieldMap.getOrDefault(sortBy, sortBy);

        Comparator<Map.Entry<Long, CollectionData>> comparator = getComparator(sortBy, isDescent);

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

    private Comparator<Map.Entry<Long, CollectionData>> getComparator(String sortBy, boolean isDescent) {

        return (e1, e2) -> {
            try {
                String s1 = (String) e1.getValue().getClass().getDeclaredMethod(sortBy).invoke(e1.getValue());
                String s2 = (String) e2.getValue().getClass().getDeclaredMethod(sortBy).invoke(e2.getValue());
                return isDescent ?
                     s2.compareTo(s1):
                     s1.compareTo(s2);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return e1.getKey().compareTo(e2.getKey());
            }
        };

    }

}


