package com.epam.speciome.catalog.domain.exceptions;

public class CollectionNotFoundException extends RuntimeException {
    public CollectionNotFoundException(long collectionId, Throwable e) {
        super("Collection not found: " + collectionId, e);
    }
}
