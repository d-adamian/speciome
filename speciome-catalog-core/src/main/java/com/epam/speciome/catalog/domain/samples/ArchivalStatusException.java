package com.epam.speciome.catalog.domain.samples;

public final class ArchivalStatusException extends RuntimeException{
    public ArchivalStatusException(String archivalStatus){
        super("Archival status not found: " + archivalStatus);
    }
}
