package com.epam.specimenbase.catalog.persistence.spring;

import com.epam.specimenbase.catalog.persistence.api.samples.SampleStorage;
import com.epam.specimenbase.catalog.persistence.api.samples.SampleStorageContract;
import com.epam.specimenbase.catalog.persistence.spring.samples.SampleJpaRepository;
import com.epam.specimenbase.catalog.persistence.spring.samples.SpringSampleStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Test Spring Data JPA sample storage implementation")
public class SpringSampleStorageTests implements SampleStorageContract {
    private SampleStorage sampleStorage;
    @Autowired
    private SampleJpaRepository sampleJpaRepository;

    @BeforeEach
    void setUp() {
        sampleStorage = new SpringSampleStorage(sampleJpaRepository);
    }

    @AfterEach
    void tearDown() {
        sampleJpaRepository.deleteAll();
    }

    @Override
    public SampleStorage sampleStorage() {
        return sampleStorage;
    }
}
