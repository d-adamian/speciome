package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.google.common.collect.ImmutableMap;

import java.time.ZonedDateTime;
import java.util.Map;

public record Sample(Long sampleId, SampleData sampleData) {

    public ZonedDateTime createdAt() {
        return sampleData.createdAt();
    }

    public ZonedDateTime updatedAt() {
        return sampleData.updatedAt();
    }

    public boolean isArchived() {
        return sampleData.isArchived();
    }

    public Map<String, String> attributes() {
        return ImmutableMap.copyOf(sampleData.attributes());
    }
}
