package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

public class UnArchiveSample {
    private final SampleStorage sampleStorage;

    public UnArchiveSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Sample unArchiveSample(Long id) {
        if (sampleStorage.getSampleById(id).isPresent()) {
            SampleData sampleData = new SampleData(
                    sampleStorage.getSampleById(id).get().createdAt(),
                    sampleStorage.getSampleById(id).get().updatedAt(),
                    sampleStorage.getSampleById(id).get().attributes(),
                    false);
            sampleStorage.updateSample(id, sampleData);

            return new Sample(id,sampleData);
        } else {
            throw new SampleNotFoundException(id);
        }
    }
}
