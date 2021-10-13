package com.epam.speciome.catalog.persistence.api.samples;

import com.google.common.collect.ImmutableMap;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleData that = (SampleData) o;
        return Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, updatedAt, attributes);
    }

    @Override
    public String toString() {
        return "SampleData{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", attributes=" + attributes +
                '}';
    }
}