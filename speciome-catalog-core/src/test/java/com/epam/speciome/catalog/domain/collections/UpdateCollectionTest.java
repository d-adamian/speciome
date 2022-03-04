package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Given that collection storage is empty")
public class UpdateCollectionTest {

    private UseCaseFactory useCaseFactory;

    @BeforeEach
    void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }


    @Nested
    @DisplayName("When I update already existing collection")
    public class UpdateExistingCollection {
        private final String originalCollectionName = "Mock_value_1";
        private final String updatedCollectionName = "Mock_value_2";
        private final CollectionAttributes attributes = new CollectionAttributes(originalCollectionName, "Mock_owner_email_value_1");

        private Long collectionId;
        private Collection originalCollection;
        private Collection updatedCollection;

        @BeforeEach
        void setUp() throws InterruptedException {
            collectionId = useCaseFactory.addCollection().addCollection(attributes);
            originalCollection = useCaseFactory.getCollection().getCollection(collectionId);
            updatedCollection = useCaseFactory.updateCollection().updateCollection(collectionId,updatedCollectionName).collection();
        }

        @Test
        @DisplayName("Then collection attributes are equal to given values")
        public void testAttributesAreSet() {
            Assertions.assertThat(updatedCollection.collectionName()).isEqualTo(updatedCollectionName);
        }


        @Test
        @DisplayName("Then returned collection is identical to collection in storage")
        public void testReturnedCollectionSameAsGetNext() {
            Collection collection = useCaseFactory.getCollection().getCollection(collectionId);
            Assertions.assertThat(collection.collectionId()).isEqualTo(collectionId);
            Assertions.assertThat(collection).isEqualTo(updatedCollection);
        }
    }



}
