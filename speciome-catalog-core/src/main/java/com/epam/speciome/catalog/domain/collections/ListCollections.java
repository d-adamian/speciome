package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.SortAttributeException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ListCollections {
    private final CollectionStorage collectionStorage;
//    private final static List<String> values = Arrays.asList("collectionName", "createdAtUtc", "updatedAtUtc", "ownerEmail");

    public ListCollections(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public List<Collection> listCollections(String sortBy, String orderBy) {

        boolean withParams = sortBy != null && orderBy != null;

        try {
            return getResult(withParams, sortBy, orderBy);
        } catch (SortAttributeException e) {
            throw new SortAttributeException(e.getMessage());
        }
    }

    public List<Collection> getResult(boolean withParams, String sortBy, String orderBy) {

        ListCollectionsResult listCollectionsResult;

        List<Collection> result = new ArrayList<>();

        if (withParams) {
            SortCollectionListParams params = new SortCollectionListParams(sortBy, orderBy);

            listCollectionsResult = collectionStorage.sortedListCollections(params.getSortAttribute(), params.isDecrease());

            Map<Long, CollectionData> resultMap = listCollectionsResult.getCollectionDataMap();

            return collectionStorage.sortedListCollections(params.getSortAttribute(), params.isDecrease())
                    .getOrder()
                    .stream()
                    .map(entry -> Collection.fromCollectionData(entry, resultMap.get(entry)))
                    .collect(Collectors.toList());


        } else {
            return collectionStorage.listCollections().getCollectionDataMap()
                    .entrySet()
                    .stream()
                    .map(entry -> Collection.fromCollectionData(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }

    }
}
