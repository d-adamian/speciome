package com.epam.speciome.catalog.persistence.spring.collections;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Entity(name = "collection")
@Table(name = "collections", schema = "catalog")
@Proxy(lazy = false)
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "collection_id")
    private long id;

    private String collectionName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static CollectionData asCollectionData(CollectionEntity entity) {
        return new CollectionData(entity.collectionName);
    }

    public static CollectionEntity fromCollectionData(CollectionData collectionData) {
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.collectionName = collectionData.collectionName();
        return collectionEntity;
    }
}
