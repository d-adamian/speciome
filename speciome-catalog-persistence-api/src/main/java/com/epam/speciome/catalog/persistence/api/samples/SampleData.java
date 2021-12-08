package com.epam.speciome.catalog.persistence.api.samples;

import com.google.common.collect.ImmutableMap;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

public record SampleData(ZonedDateTime createdAt, ZonedDateTime updatedAt,
                         Map<String, String> attributes, boolean isArchived) {
    public SampleData(ZonedDateTime createdAt, ZonedDateTime updatedAt, Map<String, String> attributes, boolean isArchived) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attributes = ImmutableMap.copyOf(attributes);
        this.isArchived = isArchived;
    }

    public Map<String, String> attributes() {
        return ImmutableMap.copyOf(attributes);
    }
}
