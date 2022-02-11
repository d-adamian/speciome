package com.epam.speciome.catalog.domain.exceptions;

public final class SampleNotFoundException extends RuntimeException {
    public SampleNotFoundException(Long sampleId, Throwable e) {
        super("Sample not found: " + sampleId, e);
    }
}
