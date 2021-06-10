package com.epam.specimenbase.catalog.ports;

import java.util.Optional;

public interface SampleStorage {
    String addSample(SampleData sampleData);

    ListSamplesResult listSamples();

    Optional<SampleData> getSampleById(String sampleId);

    void deleteSampleById(String sampleId);

    void updateSample(String sampleId, SampleData sampleData);
}
