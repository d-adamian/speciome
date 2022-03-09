package com.epam.speciome.catalog.persistence.spring.collections;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(name = "collection")
@Table(name = "collections", schema = "catalog")
@Proxy(lazy = false)
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private long id;

    @Column(name = "name")
    private String collectionName;
    private LocalDateTime createdAtUtc;
    private LocalDateTime updatedAtUtc;
    private String ownerEmail;
    private boolean archived;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CollectionData asCollectionData() {
        return new CollectionData(collectionName, Timestamp.valueOf(createdAtUtc),
                Timestamp.valueOf(updatedAtUtc), ownerEmail, archived);
    }

    public static CollectionEntity fromCollectionData(CollectionData collectionData) {
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.collectionName = collectionData.collectionName();
        collectionEntity.createdAtUtc = collectionData.createdAt().toLocalDateTime().withNano(0);
        collectionEntity.updatedAtUtc = collectionData.updatedAt().toLocalDateTime().withNano(0);
        collectionEntity.ownerEmail = collectionData.ownerEmail();
        collectionEntity.archived = collectionData.isArchived();
        return collectionEntity;
    }
}
