package com.epam.specimenbase.catalog.apiservice;

import com.epam.specimenbase.catalog.ports.ListSamplesResult;
import com.epam.specimenbase.catalog.ports.SampleData;
import com.epam.specimenbase.catalog.ports.SampleStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryMapSampleStorage implements SampleStorage {
    private int maxSampleId = 0;
    private final Map<String, SampleData> samplesById = new HashMap<>();

    @Override
    public String addSample(SampleData sampleData) {
        String sampleId = "SMPL_" + maxSampleId;
        samplesById.put(sampleId, sampleData);
        maxSampleId++;
        return sampleId;
    }

    @Override
    public ListSamplesResult listSamples() {
        return new ListSamplesResult(samplesById.size(), samplesById);
    }

    @Override
    public Optional<SampleData> getSampleById(String sampleId) {
        return Optional.ofNullable(samplesById.get(sampleId));
    }

    @Override
    public void deleteSampleById(String sampleId) {
        samplesById.remove(sampleId);
    }

    @Override
    public void updateSample(String sampleId, SampleData sampleData) {
        samplesById.put(sampleId, sampleData);
    }
}
