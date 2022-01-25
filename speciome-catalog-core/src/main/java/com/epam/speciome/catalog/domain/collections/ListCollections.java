package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;

import java.util.List;
import java.util.stream.Collectors;

public final class ListCollections {
    private final CollectionStorage collectionStorage;

    public ListCollections(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public List<Collection> listCollections() {
        ListCollectionsResult listCollectionsResult = collectionStorage.listCollections();

        return listCollectionsResult.getCollectionDataMap()
                .entrySet()
                .stream()
                .map(entry ->
                        new Collection(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
