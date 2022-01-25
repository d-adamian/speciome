package com.epam.speciome.catalog.persistence.spring;

import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorageContract;
import com.epam.speciome.catalog.persistence.spring.collections.CollectionJpaRepository;
import com.epam.speciome.catalog.persistence.spring.collections.SpringCollectionStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Test Spring Data JPA collection storage implementation")
public class SpringCollectionStorageTests implements CollectionStorageContract {
    private CollectionStorage collectionStorage;
    @Autowired
    private CollectionJpaRepository collectionJpaRepository;

    @BeforeEach
    void setUp() {
        collectionStorage = new SpringCollectionStorage(collectionJpaRepository);
    }

    @AfterEach
    void tearDown() {
        collectionJpaRepository.deleteAll();
    }

    @Override
    public CollectionStorage collectionStorage() {
        return collectionStorage;
    }
}
