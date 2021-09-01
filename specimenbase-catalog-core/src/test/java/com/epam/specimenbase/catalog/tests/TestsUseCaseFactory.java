package com.epam.specimenbase.catalog.tests;

import com.epam.specimenbase.catalog.UseCaseFactory;
import com.epam.specimenbase.catalog.persistence.testmocks.InMemoryMapSampleStorage;
import com.epam.specimenbase.catalog.persistence.testmocks.InMemoryMapUserStorage;

public final class TestsUseCaseFactory extends UseCaseFactory {
    public TestsUseCaseFactory() {
        super(new InMemoryMapUserStorage(), new InMemoryMapSampleStorage());
    }
}
