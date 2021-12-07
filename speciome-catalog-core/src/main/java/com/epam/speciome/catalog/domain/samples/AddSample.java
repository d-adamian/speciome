package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AddSample {
    static final Map<String, String> EMPTY_ATTRIBUTES = Attributes.ALL.stream()
            .collect(Collectors.toMap(Function.identity(), s -> ""));
    private final SampleStorage sampleStorage;

    public AddSample(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Result addSampleWithoutAttributes() {
        return storeSample(EMPTY_ATTRIBUTES);
    }

    public Result addSample(Map<String, String> inputAttributes) {
        Attributes.checkExtraAttributes(inputAttributes);
        return storeSample(Attributes.fillMissingAttributeValues(inputAttributes));
    }

    private Result storeSample(Map<String, String> attributes) {
        ZonedDateTime createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime updatedAt = ZonedDateTime.now(ZoneId.of("UTC"));
        Long sampleId = sampleStorage.addSample(new SampleData(createdAt, updatedAt, attributes, false));
        return new Result(sampleId);
    }

    public static final class Result {
        private final Long sampleId;

        public Result(Long sampleId) {
            this.sampleId = sampleId;
        }

        public Long getSampleId() {
            return sampleId;
        }
    }
}
