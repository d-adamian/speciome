package com.epam.specimenbase.catalog.ports;

import com.google.common.collect.ImmutableMap;

import java.time.ZonedDateTime;
import java.util.Map;

public final class SampleData {
    private final ZonedDateTime createdAt;
    private final ZonedDateTime updatedAt;
    private final Map<String, String> attributes;

    public SampleData(ZonedDateTime createdAt, ZonedDateTime updatedAt, Map<String, String> attributes) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attributes = ImmutableMap.copyOf(attributes);
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Map<String, String> getAttributes() {
        return ImmutableMap.copyOf(attributes);
    }
}
