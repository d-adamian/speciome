package com.epam.speciome.catalog.domain.exceptions;

public class AbsentCollectionNameException extends RuntimeException{
    public AbsentCollectionNameException() {
        super ("Collection name cannot be empty");
    }
}
