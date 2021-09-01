package com.epam.specimenbase.catalog.persistence.spring.samples;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleJpaRepository extends JpaRepository<SampleEntity, Long> {
    @Override
    SampleEntity getById(Long id);
}
