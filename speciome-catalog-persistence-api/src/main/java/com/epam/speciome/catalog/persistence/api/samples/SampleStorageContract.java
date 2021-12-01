package com.epam.speciome.catalog.persistence.api.samples;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

public interface SampleStorageContract {
    SampleStorage sampleStorage();

    @Test
    @DisplayName("When no samples have been added to storage. Then no samples are listed.")
    default void testNoSamplesInEmptyStorage() {
        ListSamplesResult listSamplesResult = sampleStorage().listSamples();
        Assertions.assertThat(listSamplesResult).isNotNull();
        Assertions.assertThat(listSamplesResult.getTotalCount()).isEqualTo(0);
        Assertions.assertThat(listSamplesResult.getSamplesById()).isEmpty();
    }

    @Test
    @DisplayName("When one sample has been added to storage. Then it can be retrieved back")
    default void testAddedSampleCanBeRetrieved() {
        SampleStorage sampleStorage = sampleStorage();
        SampleData sampleData = sampleOne();
        Long sampleId = sampleStorage.addSample(sampleData);
        Assertions.assertThat(sampleId).isNotNull();

        Optional<SampleData> getResult = sampleStorage.getSampleById(sampleId);
        Assertions.assertThat(getResult).isNotEmpty().get().isEqualTo(sampleData);
    }

    @Test
    @DisplayName("When two samples have been added to storage. Then both are listed")
    default void testTwoSamplesAreListed() {
        SampleStorage sampleStorage = sampleStorage();
        SampleData sampleOne = sampleOne();
        SampleData sampleTwo = sampleTwo();

        Long sampleIdOne = sampleStorage.addSample(sampleOne);
        Long sampleIdTwo = sampleStorage.addSample(sampleTwo);

        ListSamplesResult listSamplesResult = sampleStorage.listSamples();
        Assertions.assertThat(listSamplesResult).isNotNull();
        Assertions.assertThat(listSamplesResult.getTotalCount()).isEqualTo(2);
        Assertions.assertThat(listSamplesResult.getSamplesById())
                .isNotNull()
                .hasSize(2)
                .isEqualTo(Map.of(
                        sampleIdOne, sampleOne,
                        sampleIdTwo, sampleTwo
                ));
    }

    @Test
    @DisplayName("When sample have been deleted. Then it can not be found")
    default void testSampleNotFoundAfterDeletion() {
        SampleStorage sampleStorage = sampleStorage();
        SampleData sample = sampleOne();

        Long sampleId = sampleStorage.addSample(sample);
        Assertions.assertThat(sampleStorage.listSamples().getTotalCount()).isEqualTo(1);

        sampleStorage.deleteSampleById(sampleId);
        ListSamplesResult listSamplesResult = sampleStorage.listSamples();
        Assertions.assertThat(listSamplesResult.getTotalCount()).isEqualTo(0);
        Assertions.assertThat(listSamplesResult.getSamplesById()).isEmpty();
        Assertions.assertThat(sampleStorage.getSampleById(sampleId)).isEmpty();
    }

    @Test
    @DisplayName("When sample has been updated. Then it has new attributes")
    default void testSampleChangedAfterUpdate() {
        SampleStorage sampleStorage = sampleStorage();
        SampleData sampleOne = sampleOne();
        SampleData sampleTwo = sampleTwo();
        Long sampleId = sampleStorage.addSample(sampleOne);
        sampleStorage.updateSample(sampleId, sampleTwo);

        Optional<SampleData> updatedSample = sampleStorage.getSampleById(sampleId);
        Assertions.assertThat(updatedSample).isNotEmpty()
                .get().isEqualTo(sampleTwo);
    }

    private static SampleData sampleOne() {
        // we add withNano(0) to avoid failing tests for time variables
        // when there are some minor difference at the ending of output time variables
        ZonedDateTime createdAt = ZonedDateTime.now().minusDays(1).withNano(0);
        ZonedDateTime updatedAt = ZonedDateTime.now().minusHours(1).withNano(0);
        Map<String, String> attributes = Map.of("key1", "value1", "key2", "value2");
        return new SampleData(createdAt, updatedAt, attributes, false);
    }

    private static SampleData sampleTwo() {
        // we add withNano(0) to avoid failing tests for time variables
        // when there are some minor difference at the ending of output time variables
        ZonedDateTime createdAt = ZonedDateTime.now().minusDays(3).withNano(0);
        ZonedDateTime updatedAt = ZonedDateTime.now().minusMinutes(25).withNano(0);
        Map<String, String> attributes = Map.of("key3", "value3", "key4", "value4");
        return new SampleData(createdAt, updatedAt, attributes, false);
    }
}
