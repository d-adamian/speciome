package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.AbsentCollectionNameException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AddCollection {
    private final CollectionStorage collectionStorage;

    public AddCollection(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public long addCollection(CollectionAttributes attributes) {
        if (attributes.collectionName() == null) {
            throw new AbsentCollectionNameException();
        }
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));
        Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
        return collectionStorage.addCollection(new CollectionData(attributes.collectionName(), timestamp, timestamp, attributes.ownerEmail()));
    }
}
