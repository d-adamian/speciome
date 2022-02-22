package com.epam.speciome.catalog.persistence.api.collections;

import java.sql.Timestamp;

public record CollectionData(String collectionName, Timestamp createdAt, Timestamp updatedAt, String ownerEmail, boolean isArchived) {
}

