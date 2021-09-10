package com.epam.speciome.catalog.persistence.api.samples;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class ListSamplesResult {
    private final int totalCount;
    private final Map<Long, SampleData> samplesById;

    public ListSamplesResult(int totalCount, Map<Long, SampleData> samplesById) {
        this.totalCount = totalCount;
        this.samplesById = ImmutableMap.copyOf(samplesById);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public Map<Long, SampleData> getSamplesById() {
        return ImmutableMap.copyOf(samplesById);
    }
}
