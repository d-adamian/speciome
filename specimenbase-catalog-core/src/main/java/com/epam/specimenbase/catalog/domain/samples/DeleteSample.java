package com.epam.specimenbase.catalog.domain.samples;

import com.epam.specimenbase.catalog.ports.SampleStorage;

public final class DeleteSample {
    private final SampleStorage sampleStorage;

    public DeleteSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public void deleteSample(String sampleId) {
        sampleStorage.getSampleById(sampleId).ifPresentOrElse(
                sampleData -> sampleStorage.deleteSampleById(sampleId),
                () -> {
                    throw new SampleNotFoundException(sampleId);
                }
        );
    }
}
