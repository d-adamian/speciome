package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.util.Optional;

public final class GetSample {
    private final SampleStorage sampleStorage;

    public GetSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Result getSample(Long sampleId) {
        Optional<SampleData> sampleDataOptional = sampleStorage.getSampleById(sampleId);
        return sampleDataOptional
                .map(sampleData -> new Sample(sampleId, sampleData))
                .map(sample -> new Result(sampleId, sample))
                .orElseThrow(() -> new SampleNotFoundException(sampleId));
    }

    public record Result(Long sampleId, Sample sample) {
    }
}
