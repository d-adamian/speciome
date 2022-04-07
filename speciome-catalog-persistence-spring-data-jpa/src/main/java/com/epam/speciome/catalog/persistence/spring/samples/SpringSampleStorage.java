package com.epam.speciome.catalog.persistence.spring.samples;

import com.epam.speciome.catalog.persistence.api.exceptions.SampleIsNullException;
import com.epam.speciome.catalog.persistence.api.samples.ListSamplesResult;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
public class SpringSampleStorage implements SampleStorage {

    private final SampleJpaRepository sampleJpaRepository;

    public SpringSampleStorage(SampleJpaRepository sampleJpaRepository) {
        this.sampleJpaRepository = sampleJpaRepository;
    }

    @Override
    public Long addSample(SampleData sampleData) {
        SampleEntity sampleEntity = SampleEntity.fromSampleData(sampleData);
        sampleJpaRepository.saveAndFlush(sampleEntity);
        return sampleEntity.getId();
    }

    @Override
    public ListSamplesResult listSamples() {
        List<SampleEntity> sampleEntityList = sampleJpaRepository.findAll();
        Map<Long, SampleData> samplesById = sampleEntityList.stream().collect(Collectors.toMap(
                SampleEntity::getId,
                SampleEntity::asSampleData
        ));
        return new ListSamplesResult(samplesById.size(), samplesById);
    }

    @Override
    public SampleData getSampleById(Long sampleId) {
        SampleEntity sampleEntity;
        try {
            sampleEntity = sampleJpaRepository.getById(sampleId);
        } catch (EntityNotFoundException e) {
            sampleEntity = null;
        } catch (JpaObjectRetrievalFailureException e) {
            if (e.getCause() instanceof EntityNotFoundException) {
                sampleEntity = null;
            } else {
                throw e;
            }
        }
        if (sampleEntity == null) {
            throw new SampleIsNullException(sampleId);
        }
        return SampleEntity.asSampleData(sampleEntity);
    }

    @Override
    public void deleteSampleById(Long sampleId) {
        sampleJpaRepository.deleteById(sampleId);
    }

    @Override
    public void updateSample(Long sampleId, SampleData sampleData) {
        SampleEntity sampleEntity = SampleEntity.fromSampleData(sampleData);
        sampleEntity.setId(sampleId);
        sampleJpaRepository.saveAndFlush(sampleEntity);
    }

    @Override
    public List<Long> addMultipleSamples(List<SampleData> samples) {
        List<SampleEntity> entities = samples.stream()
                .map(SampleEntity::fromSampleData)
                .collect(Collectors.toList());

        List<SampleEntity> savedEntities = sampleJpaRepository.saveAll(entities);
        return savedEntities.stream().map(SampleEntity::getId)
                .collect(Collectors.toList());
    }

    @Override
    public ListSamplesResult listSamplesSortedByParams(String sortBy, boolean orderBy) {

        //TODO : make listSamplesSortedByParams() method of sampleJpaRepository in Criteria API (EPMLSTRSPM-88)
        List<SampleEntity> sampleEntityList = orderBy ?
                sampleJpaRepository.sortedSampleEntityListAcs(sortBy) :
                sampleJpaRepository.sortedSampleEntityListDesc(sortBy);

        Map<Long, SampleData> sortedSamples = new LinkedHashMap<>();
        sampleEntityList.forEach(e -> sortedSamples.put(e.getId(), SampleEntity.asSampleData(e)));

        return new ListSamplesResult(sortedSamples.size(), sortedSamples);
    }
}