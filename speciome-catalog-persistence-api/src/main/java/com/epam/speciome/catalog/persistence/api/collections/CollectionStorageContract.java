package com.epam.speciome.catalog.persistence.api.collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
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

    @Test
    @DisplayName("When no collections have been added to storage. Then no collections are listed.")
    default void testNoCollectionsInEmptyStorage() {
        ListCollectionsResult listCollectionsResult = collectionStorage().listCollections();
        Assertions.assertThat(listCollectionsResult).isNotNull();
        Assertions.assertThat(listCollectionsResult.getTotalCount()).isEqualTo(0);
        Assertions.assertThat(listCollectionsResult.getCollectionDataMap()).isEmpty();
    }

    @Test
    @DisplayName("When two collections have been added to storage. Then both are listed")
    default void testTwoCollectionsAreListed() {
        CollectionStorage collectionStorage = collectionStorage();
        CollectionData collection1 = collectionOne();
        CollectionData collection2 = collectionTwo();

        long collectionIdOne = collectionStorage.addCollection(collection1);
        long collectionIdTwo = collectionStorage.addCollection(collection2);

        ListCollectionsResult listCollectionsResult = collectionStorage.listCollections();
        Assertions.assertThat(listCollectionsResult.getCollectionDataMap())
                .isNotNull()
                .hasSize(2)
                .isEqualTo(Map.of(
                        collectionIdOne, collection1,
                        collectionIdTwo, collection2
                ));
    }

    private static CollectionData collectionOne() {
        return new CollectionData("Herbs");
    }

    private static CollectionData collectionTwo() {
        return new CollectionData("Animals");
    }
}
