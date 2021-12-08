package com.epam.speciome.catalog.domain.exceptions;

public final class ImportFileWithMissingColumns extends RuntimeException {
    public ImportFileWithMissingColumns() {
        super("Some required columns are missing in the file");
    }
}
