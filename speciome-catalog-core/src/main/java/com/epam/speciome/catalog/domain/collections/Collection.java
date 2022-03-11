package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import lombok.Builder;
import lombok.Generated;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Generated
@Value
@Builder(toBuilder = true)
public class Collection {

    @NonNull
    Long collectionId;

    @NonNull
    String collectionName;

    @NonNull
    Timestamp createdAt;

    @NonNull
    Timestamp updatedAt;

    @NonNull
    String ownerEmail;

    @Accessors(fluent = true)
    @NonNull
    Boolean isArchived;

    public static Collection fromCollectionData(long collectionId, CollectionData collectionData) {
        return Collection
                .builder()
                .collectionId(collectionId)
                .collectionName(collectionData.collectionName())
                .createdAt(collectionData.createdAt())
                .updatedAt(collectionData.updatedAt())
                .ownerEmail(collectionData.ownerEmail())
                .isArchived(collectionData.isArchived())
                .build();
    }
}
