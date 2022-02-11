package com.epam.speciome.catalog.persistence.api.exceptions;

public class CollectionIsNullException extends RuntimeException {
    public CollectionIsNullException(long collectionId) {
        super("Collection not found: " + collectionId);
    }
}
