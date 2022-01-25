package com.epam.speciome.catalog.webservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public final class ListCollectionsResponse {
    private final int totalCount;
    private final List<CollectionResponse> collections;

    public ListCollectionsResponse(int totalCount, List<CollectionResponse> collections) {
        this.totalCount = totalCount;
        this.collections = List.copyOf(collections);
    }

    @Schema(description = "Total number of collections", example = "10")
    @JsonProperty
    public int getTotalCount() {
        return totalCount;
    }

    @JsonProperty
    public List<CollectionResponse> getCollections() {
        return List.copyOf(collections);
    }
}
