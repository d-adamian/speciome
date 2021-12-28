package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotArchivedException;
import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.util.Optional;

public final class DeleteSample {
    private final SampleStorage sampleStorage;

    public DeleteSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public void deleteSample(Long sampleId) {
        Optional<SampleData> sampleById = sampleStorage.getSampleById(sampleId);

        if (sampleById.isEmpty()) {
            throw new SampleNotFoundException(sampleId);
        } else if (!sampleById.get().isArchived()) {
            throw new SampleNotArchivedException(sampleId);
        } else {
            sampleStorage.deleteSampleById(sampleId);
        }

    }
}
