package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

public final class DeleteSample {
    private final SampleStorage sampleStorage;

    public DeleteSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public void deleteSample(Long sampleId) {
        sampleStorage.getSampleById(sampleId).ifPresentOrElse(
                sampleData -> sampleStorage.deleteSampleById(sampleId),
                () -> {
                    throw new SampleNotFoundException(sampleId);
                }
        );
    }
}
