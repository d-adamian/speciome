package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ListCollections {
    private final CollectionStorage collectionStorage;

    public ListCollections(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public List<Collection> listCollections(String sortBy, String orderBy) {

        if (sortBy != null && orderBy != null) {
            return getResult(sortBy, orderBy);
        } else {
            return getResult();
        }

    }

    public List<Collection> getResult(String sortBy, String orderBy) {

        ListCollectionsResult listCollectionsResult;

        SortCollectionListParams params = new SortCollectionListParams(sortBy, orderBy);

        listCollectionsResult = collectionStorage.sortedListCollections(params.getSortAttribute(), params.isDescend());

        Map<Long, CollectionData> resultMap = listCollectionsResult.getCollectionDataMap();

        return collectionStorage.sortedListCollections(params.getSortAttribute(), params.isDescend())
                .getOrderList()
                .stream()
                .map(entry -> Collection.fromCollectionData(entry, resultMap.get(entry)))
                .collect(Collectors.toList());

    }

    public List<Collection> getResult() {
        return collectionStorage.listCollections().getCollectionDataMap()
                .entrySet()
                .stream()
                .map(entry -> Collection.fromCollectionData(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
