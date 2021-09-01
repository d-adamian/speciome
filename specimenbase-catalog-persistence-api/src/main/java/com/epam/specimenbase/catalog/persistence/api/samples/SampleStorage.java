package com.epam.specimenbase.catalog.persistence.api.samples;

import java.util.Optional;

public interface SampleStorage {
    Long addSample(SampleData sampleData);

    ListSamplesResult listSamples();

    Optional<SampleData> getSampleById(Long sampleId);

    void deleteSampleById(Long sampleId);

    void updateSample(Long sampleId, SampleData sampleData);
}
