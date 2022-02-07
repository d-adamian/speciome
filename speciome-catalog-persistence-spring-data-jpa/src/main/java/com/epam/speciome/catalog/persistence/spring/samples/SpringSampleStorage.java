package com.epam.speciome.catalog.persistence.spring.samples;

import com.epam.speciome.catalog.persistence.api.samples.ListSamplesResult;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public Optional<SampleData> getSampleById(Long sampleId) {
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
        return Optional.ofNullable(sampleEntity).map(SampleEntity::asSampleData);
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
}