package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListCollectionsTest {

    private final UseCaseFactory useCaseFactory = new TestsUseCaseFactory();

    @Test
    @DisplayName("When all collection in a list contains 0 elements ")
    public void testEmptyStorageAllCollections() {
        int sizeOfListCollections = useCaseFactory.listCollections().listCollections().size();
        assertEquals(0, sizeOfListCollections);
    }

    @Nested
    public class OneCollectionAdded {
        @BeforeEach
        public void setUp() {
            useCaseFactory.addCollection().addCollection("Brains");
        }
        @Test
        @DisplayName("There is one collection available")
        public void testStorageAllCollections() {
            int sizeOfListCollections = useCaseFactory.listCollections().listCollections().size();
            assertEquals(1, sizeOfListCollections);
        }

        @Test
        @DisplayName("The name is correct")
        public void testNameOfCollections() {
            String collectionName = useCaseFactory.listCollections().listCollections().get(0).collectionName();
            assertEquals("Brains", collectionName);
        }
    }

    @Nested
    public class TwoCollectionsAdded {
        @BeforeEach
        public void setUp() {
            useCaseFactory.addCollection().addCollection("Brains");
            useCaseFactory.addCollection().addCollection("Cats");
        }
        @Test
        @DisplayName("There are 2 elements in collections list")
        public void testStorageAllCollections() {
            int sizeOfListCollections = useCaseFactory.listCollections().listCollections().size();
            assertEquals(2, sizeOfListCollections);
        }

        @Test
        @DisplayName("The names are correct")
        public void testNameOfCollections() {
            String collectionsList = useCaseFactory.listCollections().listCollections().toString();
            assertTrue(collectionsList.contains("Brains"));
            assertTrue(collectionsList.contains("Cats"));
        }
    }

}