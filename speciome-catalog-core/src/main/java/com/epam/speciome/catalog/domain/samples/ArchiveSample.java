package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.exceptions.SampleIsNullException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class ArchiveSample {
    private final SampleStorage sampleStorage;

    public ArchiveSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Sample archiveSample(Long id) {
        try {
            SampleData sampleById = sampleStorage.getSampleById(id);

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            SampleData sampleArchived = new SampleData(
                    sampleById.createdAt(),
                    now,
                    sampleById.attributes(),
                    true);
            sampleStorage.updateSample(id, sampleArchived);
            return Sample.fromSampleData(id, sampleArchived);
        } catch (SampleIsNullException e) {
            throw new SampleNotFoundException(id, e);
        }
    }
}
