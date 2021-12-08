package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

public final class ArchiveSample {
    private final SampleStorage sampleStorage;

    public ArchiveSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Sample archiveSample(Long id) {
        if (sampleStorage.getSampleById(id).isPresent()) {
            SampleData sampleData = new SampleData(
                    sampleStorage.getSampleById(id).get().createdAt(),
                    sampleStorage.getSampleById(id).get().updatedAt(),
                    sampleStorage.getSampleById(id).get().attributes(),
                    true);
            sampleStorage.updateSample(id, sampleData);
            return new Sample(id, sampleData);
        } else {
            throw new SampleNotFoundException(id);
        }
    }
}
