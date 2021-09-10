package com.epam.speciome.catalog.tests;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.persistence.testmocks.InMemoryMapSampleStorage;
import com.epam.speciome.catalog.persistence.testmocks.InMemoryMapUserStorage;

public final class TestsUseCaseFactory extends UseCaseFactory {
    public TestsUseCaseFactory() {
        super(new InMemoryMapUserStorage(), new InMemoryMapSampleStorage());
    }
}
