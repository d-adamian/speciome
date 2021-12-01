package com.epam.speciome.catalog.domain.samples;

import java.util.Optional;

public enum ArchivalStatus {
    ALL,
    ARCHIVED,
    UNARCHIVED
    ;

    public static Optional<ArchivalStatus> defineArchivalStatus(String inputString){
        try {
            return Optional.ofNullable(inputString).map(value -> ArchivalStatus.valueOf(inputString));
        } catch (Exception e) {
            throw new ArchivalStatusException(inputString);
        }
    }
}