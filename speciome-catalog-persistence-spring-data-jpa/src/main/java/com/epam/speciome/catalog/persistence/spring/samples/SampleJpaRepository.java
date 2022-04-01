package com.epam.speciome.catalog.persistence.spring.samples;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SampleJpaRepository extends JpaRepository<SampleEntity, Long> {
    @Override
    SampleEntity getById(Long id);

    @Query("SELECT s FROM sample s INNER JOIN s.attributes a WHERE a.attribute = ?1 ORDER BY a.value ASC")
    List<SampleEntity> sortedSampleEntityListAcs(String sortBy);

    @Query("SELECT s FROM sample s INNER JOIN s.attributes a WHERE a.attribute = ?1 ORDER BY a.value DESC")
    List<SampleEntity> sortedSampleEntityListDesc(String sortBy);
}
