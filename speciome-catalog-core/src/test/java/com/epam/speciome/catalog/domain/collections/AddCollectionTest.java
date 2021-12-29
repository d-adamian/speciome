package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.AbsentCollectionNameException;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Given that collection storage is empty")
public class AddCollectionTest {
    private UseCaseFactory useCaseFactory;
    private long firstCollectionId;
    private long secondCollectionId;
    private String firstAssignedName;
    private String secondAssignedName;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
        firstAssignedName = "Ferns";
        secondAssignedName = "Reeds";
        firstCollectionId = useCaseFactory.addCollection().addCollection(firstAssignedName);
    }

    @Nested
    @DisplayName("When one collection has been added")
    public class OneCollectionAdded {

        @Test
        @DisplayName("Then that collection has non-empty identifier equal to assigned identifier")
        public void testCollectionHasNonEmptyIdentifier() {
            Collection collection = getFirstCollection();
            long collectionId = collection.collectionId();
            Assertions.assertThat(collectionId).isNotNull();
            Assertions.assertThat(collectionId).isEqualTo(firstCollectionId);
        }

        @Test
        @DisplayName("Then no collection can be retrieved by other identifier")
        public void testNoOtherCollectionCanBeRetrieved() {
            Assertions.assertThatThrownBy(() -> useCaseFactory.getCollection().getCollection(53566l))
                    .isInstanceOf(CollectionNotFoundException.class);
        }

        @Test
        @DisplayName("Then the collection name is equal to the assigned name")
        public void testStorageHasOneCollection() {
            Collection collection = getFirstCollection();
            Assertions.assertThat(collection.collectionName()).isEqualTo(firstAssignedName);
        }
    }

    @Nested
    @DisplayName("When one collection with null name value has been added")
    public class OneCollectionWithNullNameValue {
        private final String nullValue = null;

        @Test
        @DisplayName("Then exception is thrown")
        public void testExceptionIsThrown() {
            Assertions.assertThatThrownBy(() -> useCaseFactory.addCollection().addCollection(nullValue))
                    .isInstanceOf(AbsentCollectionNameException.class);
        }
    }

    @Nested
    @DisplayName("When two collections have been added")
    public class TwoCollectionsAdded {
        private long collectionIdOne;
        private long collectionIdTwo;

        @BeforeEach
        void setUp() {
            secondCollectionId = useCaseFactory.addCollection().addCollection(secondAssignedName);
            collectionIdOne = getFirstCollection().collectionId();
            collectionIdTwo = getSecondCollection().collectionId();
        }

        @Test
        @DisplayName("Then collections have different identifiers")
        public void testCollectionsHaveDifferentIdentifiers() {
            Assertions.assertThat(collectionIdOne).isNotEqualTo(collectionIdTwo);
        }

        @Test
        @DisplayName("Then both collections are present")
        public void testTwoCollectionsArePresent() {
            for (long collectionId : List.of(collectionIdOne, collectionIdTwo)) {
                Collection collection = useCaseFactory.getCollection().getCollection(collectionId);
                Assertions.assertThat(collection.collectionId()).isEqualTo(collectionId);
            }
        }
    }

    private Collection getFirstCollection() {
        return useCaseFactory.getCollection().getCollection(firstCollectionId);
    }

    private Collection getSecondCollection() {
        return useCaseFactory.getCollection().getCollection(secondCollectionId);
    }
}
