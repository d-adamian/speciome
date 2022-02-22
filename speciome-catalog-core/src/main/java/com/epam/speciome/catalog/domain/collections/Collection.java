package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;

import java.sql.Timestamp;

public record Collection(long collectionId, String collectionName, Timestamp createdAt, Timestamp updatedAt, String ownerEmail, boolean isArchived) {

    public static Collection fromCollectionData(long collectionId, CollectionData collectionData){
        return new Collection(collectionId, collectionData.collectionName(), collectionData.createdAt(),
                collectionData.updatedAt(), collectionData.ownerEmail(), collectionData.isArchived());
    }

}
