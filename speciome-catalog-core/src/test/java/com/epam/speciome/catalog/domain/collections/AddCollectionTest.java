package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.AbsentCollectionNameException;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddCollectionTest {

    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    @Nested
    @DisplayName("When one collection has been added")
    public class OneCollectionAdded {

        private long firstId;
        private Collection collection;

        @BeforeEach
        public void setUp() {
            firstId = useCaseFactory.addCollection().addCollection(new CollectionAttributes("Elm trees", "firstUser@email.com"));
            collection = useCaseFactory.getCollection().getCollection(firstId);
        }

        @Test
        @DisplayName("Then that collection has non-empty identifier equal to assigned identifier")
        public void testCollectionHasNonEmptyIdentifier() {
            Assertions.assertThat(firstId).isNotNull();
        }

        @Test
        @DisplayName("Then no collection can be retrieved by other identifier")
        public void testNoOtherCollectionCanBeRetrieved() {
            Assertions.assertThatThrownBy(() -> useCaseFactory.getCollection().getCollection(53566L))
                    .isInstanceOf(CollectionNotFoundException.class);
        }

        @Test
        @DisplayName("Then the collection name is equal to the assigned name")
        public void testStorageHasOneCollection() {
            Assertions.assertThat(collection.collectionName()).isEqualTo("Elm trees");
        }

        @Test
        @DisplayName("Then dates when collection is created and updated are equal to today's date")
        public void testCreateAndUpdateDatesAreCorrect() {
            Collection collection = useCaseFactory.getCollection().getCollection(firstId);
            LocalDateTime createdAt = collection.createdAt().toLocalDateTime();
            LocalDateTime updatedAt = collection.updatedAt().toLocalDateTime();
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

            assertTrue(Duration.between(createdAt, now).getSeconds() <= 5);
            assertTrue(Duration.between(updatedAt, now).getSeconds() <= 5);
        }
    }

    @Nested
    @DisplayName("When one collection with null name value has been added")
    public class OneCollectionWithNullNameValue {
        private final String nullValue = null;

        @Test
        @DisplayName("Then exception is thrown")
        public void testExceptionIsThrown() {
            Assertions.assertThatThrownBy(() -> useCaseFactory.addCollection()
                            .addCollection(new CollectionAttributes(nullValue, "user@mail.com")))
                    .isInstanceOf(AbsentCollectionNameException.class);
        }
    }

    @Nested
    @DisplayName("When two collections have been added")
    public class TwoCollectionsAdded {

        private long firstId;
        private long secondId;

        @BeforeEach
        public void setUp() {
            firstId = useCaseFactory.addCollection().addCollection(new CollectionAttributes("Willows", "secondUser@email.com"));
            secondId = useCaseFactory.addCollection().addCollection(new CollectionAttributes("Myrtles", "secondUser@email.com"));
        }

        @Test
        @DisplayName("Then collections have different identifiers")
        public void testCollectionsHaveDifferentIdentifiers() {
            Assertions.assertThat(firstId).isNotEqualTo(secondId);
        }

        @Test
        @DisplayName("Then both collections are present")
        public void testTwoCollectionsArePresent() {
            for (long collectionId : List.of(firstId, secondId)) {
                Collection collection = useCaseFactory.getCollection().getCollection(collectionId);
                Assertions.assertThat(collection.collectionId()).isEqualTo(collectionId);
            }
        }
    }
}
