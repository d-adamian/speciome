package com.epam.speciome.catalog.persistence.testmocks;

import com.epam.speciome.catalog.persistence.api.exceptions.SampleIsNullException;
import com.epam.speciome.catalog.persistence.api.samples.ListSamplesResult;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.util.*;
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
    public ListSamplesResult listSamplesSortedByParams(String sortBy, boolean orderBy) {

        Comparator<Map.Entry<Long, SampleData>> comparator = (e1, e2) -> {
            return orderBy ?
                    e1.getValue().attributes().get(sortBy).compareTo(e2.getValue().attributes().get(sortBy)) :
                    e2.getValue().attributes().get(sortBy).compareTo(e1.getValue().attributes().get(sortBy));
        };
        Map<Long, SampleData> resultMap = new LinkedHashMap<>();
        samplesById.entrySet().stream().sorted(comparator).forEachOrdered(e->resultMap.put(e.getKey(), e.getValue()));

        return new ListSamplesResult(resultMap.size(), resultMap);
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
