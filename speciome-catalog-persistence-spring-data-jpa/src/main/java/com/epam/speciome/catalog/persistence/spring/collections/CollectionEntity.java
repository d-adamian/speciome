package com.epam.speciome.catalog.persistence.spring.collections;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String ownerEmail;
    private boolean archived;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CollectionData asCollectionData() {
        return new CollectionData(collectionName, createdAt, updatedAt, ownerEmail, archived);
    }

    public static CollectionEntity fromCollectionData(CollectionData collectionData) {
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.collectionName = collectionData.collectionName();
        collectionEntity.createdAt = collectionData.createdAt();
        collectionEntity.updatedAt = collectionData.updatedAt();
        collectionEntity.ownerEmail = collectionData.ownerEmail();
        collectionEntity.archived = collectionData.isArchived();
        return collectionEntity;
    }
}
