package com.epam.speciome.catalog.domain.exceptions;

public class CollectionNotArchivedException extends RuntimeException {
    public CollectionNotArchivedException(Long collectionId) {
        super("Collection is not archived and cannot be deleted, id: " + collectionId);
    }
}
