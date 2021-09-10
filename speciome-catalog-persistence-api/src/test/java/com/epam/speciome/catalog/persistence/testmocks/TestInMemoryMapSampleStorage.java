package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorageContract;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Test in memory mock sample storage implementation")
public class TestInMemoryMapSampleStorage implements SampleStorageContract {
    @Override
    public SampleStorage sampleStorage() {
        return new InMemoryMapSampleStorage();
    }
}
