package com.epam.speciome.catalog.webservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedFileContentException extends RuntimeException {
    public UnsupportedFileContentException(String message) {
        super(message);
    }
}
