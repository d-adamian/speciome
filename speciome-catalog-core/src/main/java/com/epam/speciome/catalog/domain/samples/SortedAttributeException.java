package com.epam.speciome.catalog.domain.samples;

public class SortedAttributeException extends RuntimeException{
    public SortedAttributeException(String sortedAttribute) {
        super("Sorted attribute not found: " + sortedAttribute);
    }
}
