package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;

public record Collection(long collectionId, CollectionData collectionData) {

    public String collectionName() {
        return collectionData.collectionName();
    }

}
