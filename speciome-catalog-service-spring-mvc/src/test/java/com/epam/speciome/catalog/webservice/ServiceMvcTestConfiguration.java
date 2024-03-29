package com.epam.speciome.catalog.webservice;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.persistence.testmocks.InMemoryMapCollectionStorage;
import com.epam.speciome.catalog.persistence.testmocks.InMemoryMapSampleStorage;
import com.epam.speciome.catalog.persistence.testmocks.InMemoryMapUserStorage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ServiceMvcTestConfiguration {
    private final InMemoryMapSampleStorage sampleStorage = new InMemoryMapSampleStorage();
    private final InMemoryMapCollectionStorage collectionStorage = new InMemoryMapCollectionStorage();

    @Bean
    public InMemoryMapSampleStorage sampleStorage() {
        return sampleStorage;
    }

    @Bean
    public InMemoryMapCollectionStorage collectionStorage() {
        return collectionStorage;
    }

    @Bean
    public UseCaseFactory useCaseFactory() {
        return new UseCaseFactory(new InMemoryMapUserStorage(), sampleStorage, collectionStorage);
    }
}
