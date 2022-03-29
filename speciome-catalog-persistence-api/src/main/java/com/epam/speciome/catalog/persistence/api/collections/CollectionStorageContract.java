package com.epam.speciome.catalog.persistence.api.collections;

import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

public interface CollectionStorageContract {
    CollectionStorage collectionStorage();

    @Test
    @DisplayName("When no collections have been added. Then retrieving collections throws an exception")
    default void testExceptionIsThrown() {
        Assertions.assertThatThrownBy(() -> collectionStorage().getCollectionById(1))
                .isInstanceOf(CollectionIsNullException.class);
    }

    @Test
    @DisplayName("When one collection has been added to storage. Then it can be retrieved back")
    default void testAddedCollectionCanBeRetrieved() {
        CollectionStorage collectionStorage = collectionStorage();
        CollectionData collectionData = collectionOne();
        long collectionId = collectionStorage.addCollection(collectionData);
        CollectionData getResult = collectionStorage.getCollectionById(collectionId);

        Assertions.assertThat(getResult).isNotNull();
    }

    @Test
    @DisplayName("When a collection has been added to the storage. Then retrieving an another collection throws an exception")
    default void testOtherCollectionThrowsException() {
        CollectionStorage collectionStorage = collectionStorage();
        collectionStorage.addCollection(collectionOne());
        Assertions.assertThatThrownBy(() -> collectionStorage.getCollectionById(1000))
                .isInstanceOf(CollectionIsNullException.class);
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
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Timestamp timestamp = Timestamp.valueOf(now);
        return new CollectionData("Apple trees", timestamp, timestamp, "Mary Jones", false);
    }

    private static CollectionData collectionTwo() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Timestamp timestamp = Timestamp.valueOf(now);
        return new CollectionData("Bird cherry trees", timestamp, timestamp, "James Smith", false);
    }

    @Test
    @DisplayName("When collection have been deleted. Then retrieving that collection throws an exception")
    default void testCollectionNotFoundAfterDeletion() {
        CollectionStorage collectionStorage = collectionStorage();
        CollectionData collection = collectionOne();

        long collectionId = collectionStorage.addCollection(collection);
        Assertions.assertThat(collectionStorage.listCollections().getTotalCount()).isEqualTo(1);

        collectionStorage.removeCollectionById(collectionId);
        ListCollectionsResult listCollectionsResult = collectionStorage.listCollections();
        Assertions.assertThat(listCollectionsResult.getTotalCount()).isEqualTo(0);
        Assertions.assertThat(listCollectionsResult.getCollectionDataMap()).isEmpty();

        Assertions.assertThatThrownBy(() -> collectionStorage.getCollectionById(collectionId))
                .isInstanceOf(CollectionIsNullException.class);
    }

    @Test
    @DisplayName("When we get collection list with sort params. Then it return sorted list by params")
    default void testSampleSortedList() {
        CollectionStorage collectionStorage = collectionStorage();
        Long firstSampleId = collectionStorage.addSample(sampleOne());
        Long secondSampleId = collectionStorage.addSample(sampleTwo());
        Long thirdSampleId = collectionStorage.addSample(sampleThree());

        ListCollectionsResult sortedListSamplesResultByKey2Acs = collectionStorage.sortedListCollections("key2",false);
        Iterator<Map.Entry<Long, CollectionData>> iterator = sortedListSamplesResultByKey2Acs.;

        Assertions.assertThat(iterator.next().getKey()).isEqualTo(secondSampleId);
        Assertions.assertThat(iterator.next().getKey()).isEqualTo(firstSampleId);
        Assertions.assertThat(iterator.next().getKey()).isEqualTo(thirdSampleId);

        ListCollectionsResult sortedListSamplesResultByKey1Desc = collectionStorage.sortedListCollections("key1",false);
        iterator = sortedListSamplesResultByKey1Desc.loadSamplesById().entrySet().iterator();

        Assertions.assertThat(iterator.next().getKey()).isEqualTo(thirdSampleId);
        Assertions.assertThat(iterator.next().getKey()).isEqualTo(firstSampleId);
        Assertions.assertThat(iterator.next().getKey()).isEqualTo(secondSampleId);
    }
}
