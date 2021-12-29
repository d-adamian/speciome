package com.epam.speciome.catalog.webservice.models;

import com.epam.speciome.catalog.domain.collections.Collection;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public final class CollectionResponse {
    private final long collectionId;
    private final String collectionName;


    public CollectionResponse(Collection collection) {
        this.collectionId = collection.collectionId();
        this.collectionName = collection.collectionName();
    }

    @Schema(description = "Internal collection identifier", example = "1")
    @JsonProperty
    public long getCollectionId() {
        return collectionId;
    }

    @Schema(description = "Collection name", example = "Ferns")
    @JsonProperty
    public String getCollectionName() {
        return collectionName;
    }
}
