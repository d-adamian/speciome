package com.epam.specimenbase.catalog.domain.samples;

import com.epam.specimenbase.catalog.ports.SampleData;
import com.epam.specimenbase.catalog.ports.SampleStorage;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

public final class UpdateSample {
    private final SampleStorage sampleStorage;

    public UpdateSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Result updateSample(String sampleId, Map<String, String> attributes) {
        Optional<SampleData> sampleDataOptional = sampleStorage.getSampleById(sampleId);

        if (sampleDataOptional.isPresent()) {
            return updateExistingSample(sampleId, sampleDataOptional.get(), attributes);
        } else {
            throw new SampleNotFoundException(sampleId);
        }
    }

    private Result updateExistingSample(String sampleId, SampleData sampleData, Map<String, String> attributes) {
        Attributes.checkExtraAttributes(attributes);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        SampleData updatedData = new SampleData(
                sampleData.getCreatedAt(),
                now,
                Attributes.fillMissingAttributeValues(attributes)
        );
        sampleStorage.updateSample(sampleId, updatedData);
        Sample sample = Sample.fromSampleData(sampleId, updatedData);
        return new Result(sample);
    }

    public static final class Result {
        private final Sample sample;

        Result(Sample sample) {
            this.sample = sample;
        }

        public Sample getSample() {
            return sample;
        }
    }
}
