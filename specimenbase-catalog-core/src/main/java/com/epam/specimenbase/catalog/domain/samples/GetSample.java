package com.epam.specimenbase.catalog.domain.samples;

import com.epam.specimenbase.catalog.ports.SampleData;
import com.epam.specimenbase.catalog.ports.SampleStorage;

import java.util.Optional;

public final class GetSample {
    private final SampleStorage sampleStorage;

    public GetSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Result getSample(String sampleId) {
        Optional<SampleData> sampleDataOptional = sampleStorage.getSampleById(sampleId);
        return sampleDataOptional
                .map(sampleData -> Sample.fromSampleData(sampleId, sampleData))
                .map(sample -> new Result(sampleId, sample))
                .orElseThrow(() -> new SampleNotFoundException(sampleId));
    }

    public static final class Result {
        private final String sampleId;
        private final Sample sample;

        public Result(String sampleId, Sample sample) {
            this.sampleId = sampleId;
            this.sample = sample;
        }

        public String getSampleId() {
            return sampleId;
        }

        public Sample getSample() {
            return sample;
        }
    }
}
