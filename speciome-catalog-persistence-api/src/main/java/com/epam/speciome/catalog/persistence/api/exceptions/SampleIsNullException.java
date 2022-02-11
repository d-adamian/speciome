package com.epam.speciome.catalog.persistence.api.exceptions;

public class SampleIsNullException extends RuntimeException {
    public SampleIsNullException(Long sampleId) {
        super("Sample not found: " + sampleId);
    }
}
