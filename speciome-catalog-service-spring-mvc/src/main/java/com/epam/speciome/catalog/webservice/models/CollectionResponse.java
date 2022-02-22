package com.epam.speciome.catalog.webservice.models;

import com.epam.speciome.catalog.domain.collections.Collection;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

public final class CollectionResponse {
    private final long collectionId;
    private final String collectionName;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;
    private final String ownerEmail;
    private final boolean archived;


    public CollectionResponse(Collection collection) {
        this.collectionId = collection.collectionId();
        this.collectionName = collection.collectionName();
        this.createdAt = collection.createdAt();
        this.updatedAt = collection.updatedAt();
        this.ownerEmail = collection.ownerEmail();
        this.archived = collection.isArchived();
    }

    @Schema(description = "Internal collection identifier", example = "1")
    @JsonProperty
    public long getCollectionId() {
        return collectionId;
    }

    @Schema(description = "Collection name")
    @JsonProperty
    public String getCollectionName() {
        return collectionName;
    }

    @Schema(description = "Collection creation time")
    @JsonProperty public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Schema(description = "Collection update time")
    @JsonProperty public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    @Schema(description = "Email of the collection owner")
    @JsonProperty public String getOwnerEmail() {
        return ownerEmail;
    }

    @Schema(description = "Collection archival status")
    @JsonProperty public boolean isArchived() {
        return archived;
    }
}
