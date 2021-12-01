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
    private final List<SampleAttribute> attributes;
    private final boolean archive;

    public SampleResponse(Sample sample) {
        this.sampleId = sample.getSampleId();
        this.createdAt = sample.getCreatedAt();
        this.updatedAt = sample.getUpdatedAt();
        this.attributes = sample.getAttributes().entrySet().stream()
                .map(entry -> new SampleAttribute(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(SampleAttribute::getAttribute))
                .collect(Collectors.toList());
        this.archive = sample.isArchived();
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

    @Schema(description = "Sample attributes")
    @JsonProperty public List<SampleAttribute> getAttributes() {
        return attributes;
    }

    @Schema(description = "Sample archive status")
    @JsonProperty public boolean isArchive() {
        return archive;
    }
}
