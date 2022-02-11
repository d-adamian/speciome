package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.exceptions.SampleIsNullException;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

public final class GetSample {
    private final SampleStorage sampleStorage;

    public GetSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Result getSample(Long sampleId) {
        try {
            Sample sample = new Sample(sampleId, sampleStorage.getSampleById(sampleId));
            return new Result(sampleId, sample);
        } catch (SampleIsNullException e) {
            throw new SampleNotFoundException(sampleId, e);
        }
    }

    public record Result(Long sampleId, Sample sample) {
    }
}
