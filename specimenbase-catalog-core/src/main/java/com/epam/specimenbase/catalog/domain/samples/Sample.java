package com.epam.specimenbase.catalog.domain.samples;

import com.epam.specimenbase.catalog.ports.SampleData;
import com.google.common.collect.ImmutableMap;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

public final class Sample {
    private final String sampleId;
    private final ZonedDateTime createdAt;
    private final ZonedDateTime updatedAt;
    private final Map<String, String> attributes;

    private Sample(String sampleId, ZonedDateTime createdAt, ZonedDateTime updatedAt, Map<String, String> attributes) {
        this.sampleId = sampleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attributes = ImmutableMap.copyOf(attributes);
    }

    public static Sample fromSampleData(String sampleId, SampleData sampleData) {
        return new Sample(
                sampleId, sampleData.getCreatedAt(), sampleData.getUpdatedAt(), sampleData.getAttributes());
    }

    public String getSampleId() {
        return sampleId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sample sample = (Sample) o;
        return sampleId.equals(sample.sampleId) &&
                createdAt.equals(sample.createdAt) &&
                updatedAt.equals(sample.updatedAt) &&
                attributes.equals(sample.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleId, createdAt, updatedAt, attributes);
    }

    public Map<String, String> getAttributes() {
        return ImmutableMap.copyOf(attributes);
    }
}
