package com.epam.speciome.catalog.persistence.api.collections;

import java.util.Optional;

public interface CollectionStorage {
    long addCollection(CollectionData collectionData);

    ListCollectionsResult listCollections();

    Optional<CollectionData> getCollectionById(long collectionId);
}
