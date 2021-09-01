package com.epam.specimenbase.catalog.domain.samples;

public final class SampleNotFoundException extends RuntimeException {
    public SampleNotFoundException(Long sampleId) {
        super("Sample not found: " + sampleId);
    }
}
