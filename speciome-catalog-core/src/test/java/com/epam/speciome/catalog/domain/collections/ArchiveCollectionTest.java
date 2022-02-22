package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArchiveCollectionTest {
    private UseCaseFactory useCaseFactory;
    private static final int NUM_COLLECTIONS = 3;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
        for (int i = 1; i <= NUM_COLLECTIONS; i++) {
            useCaseFactory.addCollection().addCollection(new CollectionAttributes("Collection #" + i, "test@mail.ru"));
        }
    }

    @Test
    @DisplayName("Field archive should be false after creation")
    public void fieldArchiveInCollectionShouldBeFalseAfterCreation() {
        assertFalse(useCaseFactory.getCollection().getCollection(2L).isArchived());
    }

    @Test
    @DisplayName("Field archive should be true after archive")
    public void fieldArchiveInCollectionShouldBeTrueAfterArchive() {
        useCaseFactory.archiveCollection().archiveCollection(3L);
        assertTrue(useCaseFactory.getCollection().getCollection(3L).isArchived());
    }

    @Test
    @DisplayName("Should throw CollectionNotFoundException when Collection not found")
    public void throwsExceptionWhenNotFound() {
        assertThrows(CollectionNotFoundException.class, () -> useCaseFactory.archiveCollection().archiveCollection(33L));
    }

    @Test
    @DisplayName("If make Collection archive should return not Null")
    public void returnNotNullWhenMakeCollectionsAcrhive() {
        assertNotNull(useCaseFactory.archiveCollection().archiveCollection(1L));
    }


    @Test
    @DisplayName("Collection's id after creation should be the same with start conditions before creation")
    public void returnTheSameIdsAfterCreationWithStartConditions() {
        assertEquals(1L, useCaseFactory.getCollection().getCollection(1L).collectionId());
    }

    @Test
    @DisplayName("Collection's id after archive should be the same with start conditions before creation")
    public void returnTheSameIdsAfterArchiveWithStartConditions() {
        useCaseFactory.archiveCollection().archiveCollection(1L);
        assertEquals(1L, useCaseFactory.getCollection().getCollection(1L).collectionId());
    }

    @Nested
    @DisplayName("Test archiving different Collections")
    public class ArchivingCollections {
        @BeforeEach
        public void setUp() {
            for (int i = 1; i <= NUM_COLLECTIONS; i++) {
                useCaseFactory.archiveCollection().archiveCollection((long) i);
            }
        }

        @Test
        @DisplayName("Archive first collection")
        public void whenArchiveFirstCollectionShouldReturnTrueArhivedField() {
            assertTrue(useCaseFactory.getCollection().getCollection(1L).isArchived());
        }

        @Test
        @DisplayName("Archive second collection")
        public void whenArchiveSecondCollectionShouldReturnTrueArhivedField() {
            assertTrue(useCaseFactory.getCollection().getCollection(2L).isArchived());
        }

        @Test
        @DisplayName("Archive all collections")
        public void whenArchiveAllCollectionShouldReturnTrueArhivedFieldInAllOfThem() {
            assertAll(
                    () -> assertTrue(useCaseFactory.getCollection().getCollection(1L).isArchived()),
                    () -> assertTrue(useCaseFactory.getCollection().getCollection(2L).isArchived()),
                    () -> assertTrue(useCaseFactory.getCollection().getCollection(3L).isArchived())
            );
        }
    }
}
