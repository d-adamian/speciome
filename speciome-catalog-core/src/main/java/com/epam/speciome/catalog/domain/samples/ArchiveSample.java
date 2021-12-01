package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.util.Optional;

public class ArchiveSample {
    private final SampleStorage sampleStorage;

    public ArchiveSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Sample archiveSample(Long id) {
        if (sampleStorage.getSampleById(id).isPresent()) {
            SampleData sampleData = new SampleData(
                    sampleStorage.getSampleById(id).get().getCreatedAt(),
                    sampleStorage.getSampleById(id).get().getUpdatedAt(),
                    sampleStorage.getSampleById(id).get().getAttributes(),
                    true);
            sampleStorage.updateSample(id, sampleData);

            return Sample.fromSampleData(id,sampleData);
        } else {
            throw new SampleNotFoundException(id);
        }
    }
}
