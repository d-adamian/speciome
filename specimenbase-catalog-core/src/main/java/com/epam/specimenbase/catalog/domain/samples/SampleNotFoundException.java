package com.epam.specimenbase.catalog.domain.samples;

public final class SampleNotFoundException extends RuntimeException {
    public SampleNotFoundException(String sampleId) {
        super("Sample not found: " + sampleId);
    }
}
