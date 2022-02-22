package com.epam.speciome.catalog.webservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class CollectionRequest {
    private final String collectionName;

    @JsonCreator
    public CollectionRequest(@JsonProperty("collectionName") String collectionName) {
        this.collectionName = collectionName;
    }

    @Schema(description = "Collection name", example = "Myrtles")
    @JsonProperty
    public String getCollectionName() {
        return collectionName;
    }
}
