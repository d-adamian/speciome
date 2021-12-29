package com.epam.speciome.catalog.persistence.api.collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public interface CollectionStorageContract {
    CollectionStorage collectionStorage();

    @Test
    @DisplayName("When one collection has been added to storage. Then it can be retrieved back")
    default void testAddedCollectionCanBeRetrieved() {
        CollectionStorage collectionStorage = collectionStorage();
        CollectionData collectionData = collectionOne();
        long collectionId = collectionStorage.addCollection(collectionData);

        Optional<CollectionData> getResult = collectionStorage.getCollectionById(collectionId);
        Assertions.assertThat(getResult).isNotNull();
    }

    private static CollectionData collectionOne()  {
        return new CollectionData("Herbs");
    }
}
