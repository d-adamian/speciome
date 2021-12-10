package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.util.Optional;

public class UnArchiveSample {
    private final SampleStorage sampleStorage;

    public UnArchiveSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Sample unArchiveSample(Long id) {
        Optional<SampleData> sampleById = sampleStorage.getSampleById(id);
        if (sampleById.isPresent()) {
            SampleData sampleData = new SampleData(
                    sampleById.get().createdAt(),
                    sampleById.get().updatedAt(),
                    sampleById.get().attributes(),
                    false);
            sampleStorage.updateSample(id, sampleData);

            return new Sample(id,sampleData);
        } else {
            throw new SampleNotFoundException(id);
        }
    }
}
