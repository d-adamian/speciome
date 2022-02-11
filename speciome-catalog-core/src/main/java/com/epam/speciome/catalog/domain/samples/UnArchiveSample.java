package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.exceptions.SampleIsNullException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UnArchiveSample {
    private final SampleStorage sampleStorage;

    public UnArchiveSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Sample unArchiveSample(Long id) {
        try {
            SampleData sampleById = sampleStorage.getSampleById(id);

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            SampleData sampleUnarchived = new SampleData(
                    sampleById.createdAt(),
                    now,
                    sampleById.attributes(),
                    false);
            sampleStorage.updateSample(id, sampleUnarchived);
            return new Sample(id,sampleUnarchived);
        } catch (SampleIsNullException e) {
            throw new SampleNotFoundException(id, e);
        }
    }
}
