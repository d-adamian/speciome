package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListCollectionsTest {

    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void before() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    @Nested
    @DisplayName("When no collections have been added to the storage")
    public class EmptyStorage {

        @Test
        @DisplayName("Then storage does not have any collections")
        public void testEmptyStorageAllCollections() {
            int sizeOfListCollections = useCaseFactory.listCollections().listCollections("","").size();
            assertEquals(0, sizeOfListCollections);
        }
    }

    @Nested
    @DisplayName("When a collection has been added to the storage")
    public class OneCollectionAdded {

        @BeforeEach
        public void before() {
            useCaseFactory.addCollection().addCollection(new CollectionAttributes("Pines", "firstOwner@mail.com"));
        }

        @Test
        @DisplayName("Then one collection can be retrieved")
        public void testStorageAllCollections() {
            int sizeOfListCollections = useCaseFactory.listCollections().listCollections("","").size();
            assertEquals(1, sizeOfListCollections);
        }

        @Test
        @DisplayName("Then name of the retrieved collection is correct")
        public void testNameOfCollections() {
            String collectionName = useCaseFactory.listCollections().listCollections("","").get(0).getCollectionName();
            assertEquals("Pines", collectionName);
        }
    }

    @Nested
    @DisplayName("When two collections have been added to the storage")
    public class TwoCollectionsAdded {

        @BeforeEach
        public void before() {
            useCaseFactory.addCollection().addCollection(new CollectionAttributes("Yew trees", "firstOwner@mail.com"));
            useCaseFactory.addCollection().addCollection(new CollectionAttributes("Red maples", "secondOwner@mail.com"));
        }

        @Test
        @DisplayName("Then the storage contains two collections")
        public void testStorageAllCollections() {
            int sizeOfListCollections = useCaseFactory.listCollections().listCollections("","").size();
            assertEquals(2, sizeOfListCollections);
        }

        @Test
        @DisplayName("Then names of retrieved collections are correct")
        public void testNameOfCollections() {
           List<Collection> retrievedCollections = useCaseFactory.listCollections().listCollections("","");

            assertTrue(retrievedCollections.stream().anyMatch(collection -> collection.getCollectionName().equals("Yew trees")));
            assertTrue(retrievedCollections.stream().anyMatch(collection -> collection.getCollectionName().equals("Red maples")));
        }
    }

    @Nested
    @DisplayName("When the storage contains list of collections")
    public class SortedCollectionListTest{


        @Test
        public void testSortedCollectionList(){
            List<Collection> sortedCollectionList = useCaseFactory.listCollections().listCollections("id","asc");
        }
    }
}