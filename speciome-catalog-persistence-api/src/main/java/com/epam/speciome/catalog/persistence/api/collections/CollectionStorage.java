package com.epam.speciome.catalog.persistence.api.collections;

import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;

public interface CollectionStorage {
    long addCollection(CollectionData collectionData);

    ListCollectionsResult listCollections();

    /**
     *
     * @throws CollectionIsNullException
     * if a Collection with the given id cannot be found
     */
    CollectionData getCollectionById(long collectionId);

    void updateCollection(Long collectionId, CollectionData collectionData);

    void removeCollectionById(Long collectionId);
}
