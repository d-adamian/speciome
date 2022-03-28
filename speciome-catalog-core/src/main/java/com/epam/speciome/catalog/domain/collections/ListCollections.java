package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;

import java.util.*;
import java.util.stream.Collectors;

public final class ListCollections {
    private final CollectionStorage collectionStorage;
    private final static List<String> values = Arrays.asList("collectionName", "createdAtUtc", "updatedAtUtc", "ownerEmail");

    public ListCollections(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public List<Collection> listCollections(String sortBy, String orderBy) {
        ListCollectionsResult listCollectionsResult;
        if (values.contains(sortBy)) {
            listCollectionsResult = collectionStorage.sortedListCollections(sortBy, orderBy);

        } else {
            listCollectionsResult = collectionStorage.listCollections();
        }

        List<Collection> result = new ArrayList<>();

        List<Long> order =  listCollectionsResult.getOrder();

        Map<Long, CollectionData> map = listCollectionsResult.getCollectionDataMap();

        for (int i = 0; i < order.size(); i++){
            long tmp = order.get(i);
            result.add(Collection.fromCollectionData(tmp,map.get(tmp)));
        }
        //TODO somthng with for
        return result;

    }
}
