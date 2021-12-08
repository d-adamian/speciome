package com.epam.speciome.catalog.webservice.models;

import com.epam.speciome.catalog.domain.samples.Sample;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class SampleResponse {
    private final Long sampleId;
    private final ZonedDateTime createdAt;
    private final ZonedDateTime updatedAt;
    private final boolean archived;
    private final List<SampleAttribute> attributes;

    public SampleResponse(Sample sample) {
        this.sampleId = sample.sampleId();
        this.createdAt = sample.createdAt();
        this.updatedAt = sample.updatedAt();
        this.archived = sample.isArchived();
        this.attributes = sample.attributes().entrySet().stream()
                .map(entry -> new SampleAttribute(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(SampleAttribute::getAttribute))
                .collect(Collectors.toList());
    }

    @Schema(description = "Internal sample identifier", example = "1")
    @JsonProperty
    public Long getSampleId() {
        return sampleId;
    }

    @Schema(description = "Sample creation time")
    @JsonProperty public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    @Schema(description = "Sample update time")
    @JsonProperty public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Schema(description = "Sample archival status")
    @JsonProperty public boolean isArchived() {
        return archived;
    }

    @Schema(description = "Sample attributes")
    @JsonProperty public List<SampleAttribute> getAttributes() {
        return attributes;
    }
}
