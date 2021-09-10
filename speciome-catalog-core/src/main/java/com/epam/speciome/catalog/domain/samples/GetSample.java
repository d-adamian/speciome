package com.epam.speciome.catalog.domain.samples;

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
                .map(sampleData -> Sample.fromSampleData(sampleId, sampleData))
                .map(sample -> new Result(sampleId, sample))
                .orElseThrow(() -> new SampleNotFoundException(sampleId));
    }

    public static final class Result {
        private final Long sampleId;
        private final Sample sample;

        public Result(Long sampleId, Sample sample) {
            this.sampleId = sampleId;
            this.sample = sample;
        }

        public Long getSampleId() {
            return sampleId;
        }

        public Sample getSample() {
            return sample;
        }
    }
}
