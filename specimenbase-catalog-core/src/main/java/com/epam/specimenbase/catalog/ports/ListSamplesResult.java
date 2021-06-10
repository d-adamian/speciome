package com.epam.specimenbase.catalog.ports;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class ListSamplesResult {
    private final int totalCount;
    private final Map<String, SampleData> samplesById;

    public ListSamplesResult(int totalCount, Map<String, SampleData> samplesById) {
        this.totalCount = totalCount;
        this.samplesById = ImmutableMap.copyOf(samplesById);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public Map<String, SampleData> getSamplesById() {
        return ImmutableMap.copyOf(samplesById);
    }
}
