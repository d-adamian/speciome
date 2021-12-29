package com.epam.speciome.catalog.domain.exceptions;

public class CollectionNotFoundException extends RuntimeException {
    public CollectionNotFoundException(long collectionId) {
        super("Collection not found: " + collectionId);
    }
}
