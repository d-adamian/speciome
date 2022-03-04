package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UpdateCollection {

    private final CollectionStorage collectionStorage;

    public UpdateCollection(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public Result updateCollection(long collectionID, String newCollectionName) {
        try {
            CollectionData collectionData = collectionStorage.getCollectionById(collectionID);
            return updateExistingCollection(collectionID,  newCollectionName, collectionData);
        } catch (CollectionIsNullException e) {
            throw new CollectionNotFoundException(collectionID, e);
        }

    }

    private Result updateExistingCollection(Long collectionId, String newCollectionName, CollectionData collectionData) {

        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));
        Timestamp now = Timestamp.valueOf(zdt.toLocalDateTime());

        CollectionData updatedData = new CollectionData(newCollectionName,
                collectionData.createdAt(),
                now,
                collectionData.ownerEmail(),
                collectionData.isArchived());
        collectionStorage.updateCollection(collectionId, updatedData);
        Collection collection = Collection.fromCollectionData(collectionId, updatedData);

        return new Result(collection);
    }

    public record Result(Collection collection) {
    }
}
