package com.epam.speciome.catalog.domain.exceptions;

public class SortAttributeException extends RuntimeException {
        public SortAttributeException(String sortAttribute) {
            super("Sort attribute not found: " + sortAttribute);
        }

}
