package com.epam.specimenbase.catalog.webservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public final class ListSamplesResponse {
    private final int totalCount;
    private final List<SampleResponse> samples;

    public ListSamplesResponse(int totalCount, List<SampleResponse> samples) {
        this.totalCount = totalCount;
        this.samples = List.copyOf(samples);
    }

    @Schema(description = "Total number of samples", example = "10")
    @JsonProperty
    public int getTotalCount() {
        return totalCount;
    }

    @JsonProperty
    public List<SampleResponse> getSamples() {
        return List.copyOf(samples);
    }
}
