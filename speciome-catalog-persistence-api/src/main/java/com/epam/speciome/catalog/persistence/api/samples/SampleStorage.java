package com.epam.speciome.catalog.persistence.api.samples;

import java.util.List;
import java.util.Optional;

public interface SampleStorage {
    Long addSample(SampleData sampleData);

    ListSamplesResult listSamples();

    Optional<SampleData> getSampleById(Long sampleId);

    void deleteSampleById(Long sampleId);

    void updateSample(Long sampleId, SampleData sampleData);

    List<Long> addMultipleSamples(List<SampleData> samples);
}
