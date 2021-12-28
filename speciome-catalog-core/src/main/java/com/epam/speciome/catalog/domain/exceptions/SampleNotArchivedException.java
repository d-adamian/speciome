package com.epam.speciome.catalog.domain.exceptions;

public final class SampleNotArchivedException extends RuntimeException{
    public SampleNotArchivedException(Long sampleId){
        super("Sample is not archived and cannot be deleted, id: " + sampleId);
    }
}
