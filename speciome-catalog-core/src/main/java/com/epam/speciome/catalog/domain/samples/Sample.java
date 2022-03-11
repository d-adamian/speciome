package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import lombok.Builder;
import lombok.Generated;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.Map;

@Generated
@Value
@Builder(toBuilder = true)
public class Sample {

    @NonNull
    Long sampleId;

    @NonNull
    ZonedDateTime createdAt;

    @NonNull
    ZonedDateTime updatedAt;

    @NonNull
    Map<String, String> attributes;

    @Accessors(fluent = true)
    @NonNull
    Boolean isArchived;

    public static Sample fromSampleData(long sampleId, SampleData sampleData) {
        return Sample
                .builder()
                .sampleId(sampleId)
                .createdAt(sampleData.createdAt())
                .updatedAt(sampleData.updatedAt())
                .attributes(sampleData.attributes())
                .isArchived(sampleData.isArchived())
                .build();
    }
}
