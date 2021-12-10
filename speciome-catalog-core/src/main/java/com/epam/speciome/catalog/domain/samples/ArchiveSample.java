package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.util.Optional;

public final class ArchiveSample {
    private final SampleStorage sampleStorage;

    public ArchiveSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Sample archiveSample(Long id) {
        Optional<SampleData> sampleById = sampleStorage.getSampleById(id);
        if (sampleById.isPresent()) {
            SampleData sampleData = new SampleData(
                    sampleById.get().createdAt(),
                    sampleById.get().updatedAt(),
                    sampleById.get().attributes(),
                    true);
            sampleStorage.updateSample(id, sampleData);
            return new Sample(id, sampleData);
        } else {
            throw new SampleNotFoundException(id);
        }
    }
}
