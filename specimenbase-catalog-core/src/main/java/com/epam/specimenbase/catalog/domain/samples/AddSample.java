package com.epam.specimenbase.catalog.domain.samples;

import com.epam.specimenbase.catalog.ports.SampleData;
import com.epam.specimenbase.catalog.ports.SampleStorage;
import org.checkerframework.checker.units.qual.A;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
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

    public Result addNewSampleWithoutAttributes() {
        return storeNewSample(EMPTY_ATTRIBUTES);
    }

    public Result addNewSample(Map<String, String> inputAttributes) {
        Attributes.checkExtraAttributes(inputAttributes);
        return storeNewSample(Attributes.fillMissingAttributeValues(inputAttributes));
    }

    private Result storeNewSample(Map<String, String> attributes) {
        ZonedDateTime createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime updatedAt = ZonedDateTime.now(ZoneId.of("UTC"));
        String sampleId = sampleStorage.addSample(new SampleData(createdAt, updatedAt, attributes));
        return new Result(sampleId);
    }

    public static final class Result {
        private final String sampleId;

        public Result(String sampleId) {
            this.sampleId = sampleId;
        }

        public String getSampleId() {
            return sampleId;
        }
    }
}
