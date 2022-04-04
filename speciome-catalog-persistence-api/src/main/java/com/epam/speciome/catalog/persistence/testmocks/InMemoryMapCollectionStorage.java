package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;

import java.util.*;
import java.util.function.Function;

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
        Comparator<Map.Entry<Long, CollectionData>> comparator = switch (sortBy) {
            case "collectionName" -> (e1, e2) ->
                    doCompare(e2, e1, (Map.Entry<Long, CollectionData> x) -> x.getValue().collectionName(), isDescent);
            case "createdAtUtc" -> (e1, e2) ->
                    doCompare(e2, e1, (Map.Entry<Long, CollectionData> x) -> x.getValue().createdAt(), isDescent);
            case "updatedAtUtc" -> (e1, e2) ->
                    doCompare(e2, e1, (Map.Entry<Long, CollectionData> x) -> x.getValue().updatedAt(), isDescent);
            case "ownerEmail" -> (e1, e2) ->
                    doCompare(e2, e1, (Map.Entry<Long, CollectionData> x) -> x.getValue().ownerEmail(), isDescent);
            default -> (e1, e2) ->
                    doCompare(e2, e1, (Map.Entry<Long, CollectionData> x) -> x.getKey(), isDescent);
        };

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

    private <T> int doCompare(T a, T b, Function<T, Comparable> f, boolean desc) {
        return desc ? f.apply(a).compareTo(f.apply(b)) : f.apply(b).compareTo(f.apply(a));
    }
}
