package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.exceptions.SampleIsNullException;
import com.epam.speciome.catalog.persistence.api.samples.ListSamplesResult;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryMapSampleStorage implements SampleStorage {
    private long maxSampleId = 0;
    private final Map<Long, SampleData> samplesById = new HashMap<>();

    @Override
    public Long addSample(SampleData sampleData) {
        Long sampleId = maxSampleId;
        samplesById.put(sampleId, sampleData);
        maxSampleId++;
        return sampleId;
    }

    @Override
    public ListSamplesResult listSamples() {
        return new ListSamplesResult(samplesById.size(), samplesById);
    }

    @Override
    public SampleData getSampleById(Long sampleId) {
        SampleData sampleData = samplesById.get(sampleId);
        if (sampleData == null) {
            throw new SampleIsNullException(sampleId);
        }
        return sampleData;
    }

    @Override
    public void deleteSampleById(Long sampleId) {
        samplesById.remove(sampleId);
    }

    @Override
    public void updateSample(Long sampleId, SampleData sampleData) {
        samplesById.put(sampleId, sampleData);
    }

    @Override
    public List<Long> addMultipleSamples(List<SampleData> samples) {
        return samples.stream().map(this::addSample).collect(Collectors.toList());
    }

    public void clear() {
        samplesById.clear();
    }
}
