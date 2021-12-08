package com.epam.speciome.catalog.persistence.api.samples;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public record ListSamplesResult(int totalCount,
                                Map<Long, SampleData> loadSamplesById) {
    public ListSamplesResult(int totalCount, Map<Long, SampleData> loadSamplesById) {
        this.totalCount = totalCount;
        this.loadSamplesById = ImmutableMap.copyOf(loadSamplesById);
    }

    public Map<Long, SampleData> loadSamplesById() {
        return ImmutableMap.copyOf(loadSamplesById);
    }
}
