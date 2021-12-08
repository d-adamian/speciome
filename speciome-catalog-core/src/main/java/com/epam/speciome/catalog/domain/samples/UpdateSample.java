package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

public final class UpdateSample {
    private final SampleStorage sampleStorage;

    public UpdateSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Result updateSample(Long sampleId, Map<String, String> attributes) {
        Optional<SampleData> sampleDataOptional = sampleStorage.getSampleById(sampleId);

        if (sampleDataOptional.isPresent()) {
            return updateExistingSample(sampleId, sampleDataOptional.get(), attributes);
        } else {
            throw new SampleNotFoundException(sampleId);
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
