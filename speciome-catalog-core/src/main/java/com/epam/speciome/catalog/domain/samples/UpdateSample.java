package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.exceptions.SampleIsNullException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

public final class UpdateSample {
    private final SampleStorage sampleStorage;

    public UpdateSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Result updateSample(Long sampleId, Map<String, String> attributes) {
       try {
           return updateExistingSample(sampleId, sampleStorage.getSampleById(sampleId), attributes);
       } catch (SampleIsNullException e) {
           throw new SampleNotFoundException(sampleId, e);
       }
    }

    private Result updateExistingSample(Long sampleId, SampleData sampleData, Map<String, String> attributes) {
        Attributes.checkExtraAttributes(attributes);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        SampleData updatedData = new SampleData(
                sampleData.createdAt(),
                now,
                Attributes.fillMissingAttributeValues(attributes),
                sampleData.isArchived()
        );
        sampleStorage.updateSample(sampleId, updatedData);
        Sample sample = new Sample(sampleId, updatedData);
        return new Result(sample);
    }

    public record Result(Sample sample) {
    }
}
