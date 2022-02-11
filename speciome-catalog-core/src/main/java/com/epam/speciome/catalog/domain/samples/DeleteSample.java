package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotArchivedException;
import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.exceptions.SampleIsNullException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

public final class DeleteSample {
    private final SampleStorage sampleStorage;

    public DeleteSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public void deleteSample(Long sampleId) {
        try {
            SampleData sampleById = sampleStorage.getSampleById(sampleId);
            if (sampleById.isArchived()) {
                sampleStorage.deleteSampleById(sampleId);
            } else {
                throw new SampleNotArchivedException(sampleId);
            }
        } catch (SampleIsNullException e) {
            throw new SampleNotFoundException(sampleId, e);
        }
    }
}
