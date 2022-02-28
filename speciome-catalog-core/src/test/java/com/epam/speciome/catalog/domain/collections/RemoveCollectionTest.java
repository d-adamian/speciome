package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotArchivedException;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Given that collection is empty")
public class RemoveCollectionTest {
    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    @Nested
    @DisplayName("When I try to delete collection")
    public class DeleteFromEmptyCollection {

        @Test
        @DisplayName("Then exception is thrown")
        public void testExceptionIsThrown() {
            Long collectionId = 12343534L;
            Assertions.assertThatThrownBy(() -> useCaseFactory.removeCollection().removeCollection(collectionId))
                    .isInstanceOf(CollectionNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("When I have created a collection")
    public class DeleteCreatedCollection {
        Long collectionId;

        @BeforeEach
        public void setUp() {
            collectionId = useCaseFactory.addCollection().addCollection(new CollectionAttributes("Elm trees", "firstUser@email.com"));
        }

        @Test
        @DisplayName("Then throw exception if it is not archived")
        public void testThrowExceptionIfNotArchived() {
            assertThrows(CollectionNotArchivedException.class, () ->
                    useCaseFactory.removeCollection().removeCollection(collectionId));
        }

        @Test
        @DisplayName("When element is deleted it then list of Collections is empty")
        public void testSuccessfulDeletion() {
            useCaseFactory.archiveCollection().archiveCollection(collectionId);
            useCaseFactory.removeCollection().removeCollection(collectionId);
            assertTrue(useCaseFactory.listCollections().listCollections().isEmpty());
        }
    }
}
