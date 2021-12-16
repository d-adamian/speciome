package com.epam.speciome.catalog.domain.exceptions;

public final class ImportFileWithMissingColumnsException extends RuntimeException {
    public ImportFileWithMissingColumnsException() {
        super("Some required columns are missing in the file");
    }
}

