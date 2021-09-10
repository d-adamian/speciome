package com.epam.speciome.catalog.domain.samples;

public final class UnexpectedAttributeException extends RuntimeException {
    public UnexpectedAttributeException(String attribute) {
        super("Unexpected attribute: " + attribute);
    }
}
