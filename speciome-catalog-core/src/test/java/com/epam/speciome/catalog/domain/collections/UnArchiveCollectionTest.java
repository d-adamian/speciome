package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnArchiveCollectionTest {
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
    @DisplayName("Field archive should be false after unArchive")
    public void fieldArchiveInCollectionShouldBeFalseAfterUnArchive() {
        useCaseFactory.archiveCollection().archiveCollection(3L);
        useCaseFactory.unArchiveCollection().unArchiveCollection(3L);
        assertFalse(useCaseFactory.getCollection().getCollection(3L).isArchived());
    }

    @Test
    @DisplayName("Should throw CollectionNotFoundException when Collection not found")
    public void throwsExceptionWhenNotFound() {
        assertThrows(CollectionNotFoundException.class, () -> useCaseFactory.unArchiveCollection().unArchiveCollection(33L));
    }

    @Test
    @DisplayName("If make Collection unarchive should return not Null")
    public void returnNotNullWhenMakeCollectionsUnAcrhive() {
        assertNotNull(useCaseFactory.unArchiveCollection().unArchiveCollection(1L));
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

    @Test
    @DisplayName("Collection's id after unarchive should be the same with start conditions before creation")
    public void returnTheSameIdsAfterUnArchiveWithStartConditions() {
        useCaseFactory.archiveCollection().archiveCollection(1L);
        useCaseFactory.unArchiveCollection().unArchiveCollection(1L);
        assertEquals(1L, useCaseFactory.getCollection().getCollection(1L).collectionId());
    }

    @Nested
    @DisplayName("Test unarchiving different Collections")
    public class UnArchivingCollections {

        @BeforeEach
        @DisplayName("make all collections archive")
        public void setUp() {
            for (int i = 1; i <= NUM_COLLECTIONS; i++) {
                useCaseFactory.archiveCollection().archiveCollection((long) i);
            }
        }

        @Test
        @DisplayName("Unarchive first collection")
        public void whenUnArchiveFirstCollectionShouldReturnFalseArhivedField() {
            useCaseFactory.unArchiveCollection().unArchiveCollection(1L);
            assertFalse(useCaseFactory.getCollection().getCollection(1L).isArchived());
        }

        @Test
        @DisplayName("Unarchive second collection")
        public void whenUnArchiveSecondCollectionShouldReturnFalseArhivedField() {
            useCaseFactory.unArchiveCollection().unArchiveCollection(2L);
            assertFalse(useCaseFactory.getCollection().getCollection(2L).isArchived());
        }

        @Test
        @DisplayName("Unarchive all collections")
        public void whenUnArchiveAllCollectionShouldReturnFalseArhivedFieldInAllOfThem() {

            useCaseFactory.unArchiveCollection().unArchiveCollection(1L);
            useCaseFactory.unArchiveCollection().unArchiveCollection(2L);
            useCaseFactory.unArchiveCollection().unArchiveCollection(3L);

            assertAll(
                    () -> assertFalse(useCaseFactory.getCollection().getCollection(1L).isArchived()),
                    () -> assertFalse(useCaseFactory.getCollection().getCollection(2L).isArchived()),
                    () -> assertFalse(useCaseFactory.getCollection().getCollection(3L).isArchived())
            );
        }
    }
}
