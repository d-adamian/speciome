package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorageContract;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Test in memory mock collection storage implementation")
public class TestInMemoryMapCollectionStorage implements CollectionStorageContract {
    @Override
    public CollectionStorage collectionStorage() {
        return new InMemoryMapCollectionStorage();
    }
}
