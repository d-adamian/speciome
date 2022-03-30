package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.domain.exceptions.SortAttributeException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.checkerframework.checker.units.qual.C;
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
            int sizeOfListCollections = useCaseFactory.listCollections().listCollections(null,null).size();
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
            int sizeOfListCollections = useCaseFactory.listCollections().listCollections(null,null).size();
            assertEquals(1, sizeOfListCollections);
        }

        @Test
        @DisplayName("Then name of the retrieved collection is correct")
        public void testNameOfCollections() {
            String collectionName = useCaseFactory.listCollections().listCollections(null,null).get(0).getCollectionName();
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
            int sizeOfListCollections = useCaseFactory.listCollections().listCollections(null,null).size();
            assertEquals(2, sizeOfListCollections);
        }

        @Test
        @DisplayName("Then names of retrieved collections are correct")
        public void testNameOfCollections() {
           List<Collection> retrievedCollections = useCaseFactory.listCollections().listCollections(null,null);

            assertTrue(retrievedCollections.stream().anyMatch(collection -> collection.getCollectionName().equals("Yew trees")));
            assertTrue(retrievedCollections.stream().anyMatch(collection -> collection.getCollectionName().equals("Red maples")));
        }
    }

    @Nested
    @DisplayName("When the storage contains list of collections")
    public class SortedCollectionListTest{

        long firstCollectionId;
        long secondCollectionId;
        long thirdCollectionId;

        @BeforeEach
        public void setUp() {
            firstCollectionId = useCaseFactory.addCollection().addCollection(new CollectionAttributes("1st_mock_collection_name","3d_mock_collection_owner"));
            secondCollectionId = useCaseFactory.addCollection().addCollection(new CollectionAttributes("2nd_mock_collection_name","1st_mock_collection_owner"));
            thirdCollectionId = useCaseFactory.addCollection().addCollection(new CollectionAttributes("3d_mock_collection_name","2nd_mock_collection_owner"));
        }

        @Test
        public void testSortCollectionListByCollectionNameAsc(){
            List<Collection> sortedCollectionList = useCaseFactory.listCollections().listCollections("collectionName","asc");
            assertEquals((long) sortedCollectionList.get(0).getCollectionId(), firstCollectionId);
            assertEquals((long) sortedCollectionList.get(1).getCollectionId(), secondCollectionId);
            assertEquals((long) sortedCollectionList.get(2).getCollectionId(), thirdCollectionId);
        }
        @Test
        public void testSortCollectionListByCollectionNameDesc(){
            List<Collection> sortedCollectionList = useCaseFactory.listCollections().listCollections("collectionName","desc");
            assertEquals((long) sortedCollectionList.get(0).getCollectionId(), thirdCollectionId);
            assertEquals((long) sortedCollectionList.get(1).getCollectionId(), secondCollectionId);
            assertEquals((long) sortedCollectionList.get(2).getCollectionId(), firstCollectionId);
        }
        @Test
        public void testSortCollectionListByOwnerEmailAsc(){
            List<Collection> sortedCollectionList = useCaseFactory.listCollections().listCollections("ownerEmail","asc");
            assertEquals((long) sortedCollectionList.get(0).getCollectionId(), secondCollectionId);
            assertEquals((long) sortedCollectionList.get(1).getCollectionId(), thirdCollectionId);
            assertEquals((long) sortedCollectionList.get(2).getCollectionId(), firstCollectionId);
        }
        @Test
        public void testSortCollectionListByOwnerEmailDesc(){
            List<Collection> sortedCollectionList = useCaseFactory.listCollections().listCollections("ownerEmail","desc");
            assertEquals((long) sortedCollectionList.get(0).getCollectionId(), firstCollectionId);
            assertEquals((long) sortedCollectionList.get(1).getCollectionId(), thirdCollectionId);
            assertEquals((long) sortedCollectionList.get(2).getCollectionId(), secondCollectionId);
        }
        @Test
        public void assertThatSortCollectionListThrowsSortAttributeException(){
            Assertions.assertThrows(SortAttributeException.class,
                    ()-> new SortCollectionListParams("incorrect_mock_sort_attribute_value","asc"));
            Assertions.assertThrows(SortAttributeException.class,
                    ()-> new SortCollectionListParams("collectionName","incorrect_mock_order_attribute_value"));
            Assertions.assertThrows(SortAttributeException.class,
                    ()-> new SortCollectionListParams("super_incorrect_mock_attribute_value","super_incorrect_mock_attribute_value"));

        }
    }
}