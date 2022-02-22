package com.epam.speciome.catalog.persistence.spring.collections;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.exceptions.CollectionIsNullException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
public class SpringCollectionStorage implements CollectionStorage {
    private final CollectionJpaRepository collectionJpaRepository;

    public SpringCollectionStorage(CollectionJpaRepository collectionJpaRepository) {
        this.collectionJpaRepository = collectionJpaRepository;
    }

    @Override
    public long addCollection(CollectionData collectionData) {
        CollectionEntity collectionEntity = CollectionEntity.fromCollectionData(collectionData);
        collectionJpaRepository.saveAndFlush(collectionEntity);
        return collectionEntity.getId();
    }

    @Override
    public ListCollectionsResult listCollections() {
        List<CollectionEntity> collectionEntityList = collectionJpaRepository.findAll();
        Map<Long, CollectionData> collectionDataMap = collectionEntityList
                .stream()
                .collect(Collectors.toMap(
                        CollectionEntity::getId,
                        CollectionEntity::asCollectionData
                ));
        return new ListCollectionsResult(collectionDataMap.size(), collectionDataMap);
    }

    @Override
    public CollectionData getCollectionById(long collectionId) {
        CollectionEntity collectionEntity;
        try {
            collectionEntity = collectionJpaRepository.getById(collectionId);
        } catch (EntityNotFoundException e) {
            collectionEntity = null;
        } catch (JpaObjectRetrievalFailureException e) {
            if (e.getCause() instanceof EntityNotFoundException) {
                collectionEntity = null;
            } else {
                throw e;
            }
        }
        if (collectionEntity == null) {
            throw new CollectionIsNullException(collectionId);
        }
        return collectionEntity.asCollectionData();
    }

    @Override
    public void updateCollection(Long collectionId, CollectionData collectionData) {
        CollectionEntity collectionEntity = CollectionEntity.fromCollectionData(collectionData);
        collectionEntity.setId(collectionId);
        collectionJpaRepository.saveAndFlush(collectionEntity);
    }
}
