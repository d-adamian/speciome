package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.persistence.api.samples.ListSamplesResult;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ListSamplesTest {

    private UseCaseFactory useCaseFactory;

    @BeforeEach
    void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    @Nested
    @DisplayName("When the storage is empty")
    public class ListFromEmptyStorage {

        @Test
        @DisplayName("Then all samples list contains 0 elements ")
        public void testEmptyStorageAllSamples() {
            ListSamples.Result listResult =
                    useCaseFactory.listSamples().listSamples();
            int samplesCount = listResult.totalCount();
            Assertions.assertThat(samplesCount).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("When the storage contains a single unarchived sample")
    public class ListSingleUnarchivedSample {

        long sampleId;

        @BeforeEach
        public void setUp() {
            sampleId = useCaseFactory.addSample().addSampleWithoutAttributes().sampleId();
        }

        @ParameterizedTest
        @EnumSource(ArchivalStatus.class)
        public void testCaseUnarchivedSample(ArchivalStatus archivalStatus) {
            ListSamples.Result listResult =
                    useCaseFactory.listSamples().listSamples(Optional.of(archivalStatus));
            int samplesCount = listResult.totalCount();
            if (archivalStatus.equals(ArchivalStatus.ARCHIVED)) {
                Assertions.assertThat(samplesCount).isEqualTo(0);
            } else if (archivalStatus.equals(ArchivalStatus.ALL) || archivalStatus.equals(ArchivalStatus.UNARCHIVED)){
                long resultId = listResult.samples().get(0).getSampleId();
                Assertions.assertThat(samplesCount).isEqualTo(1);
                Assertions.assertThat(sampleId).isEqualTo(resultId);
            }
        }
    }

    @Nested
    @DisplayName("When the storage contains a single archived sample")
    public class ListSingleArchivedSample {

        long sampleId;

        @BeforeEach
        public void setUp() {
            sampleId = useCaseFactory.addSample().addSampleWithoutAttributes().sampleId();
            useCaseFactory.archiveSample().archiveSample(sampleId);
        }

        @ParameterizedTest
        @EnumSource(ArchivalStatus.class)
        public void testCaseArchivedSample(ArchivalStatus archivalStatus) {
            ListSamples.Result listResult =
                    useCaseFactory.listSamples().listSamples(Optional.of(archivalStatus));
            int samplesCount = listResult.totalCount();
            if (archivalStatus.equals(ArchivalStatus.UNARCHIVED)) {
                Assertions.assertThat(samplesCount).isEqualTo(0);
            } else if (archivalStatus.equals(ArchivalStatus.ALL) || archivalStatus.equals(ArchivalStatus.ARCHIVED)) {
                long resultId = listResult.samples().get(0).getSampleId();
                Assertions.assertThat(samplesCount).isEqualTo(1);
                Assertions.assertThat(sampleId).isEqualTo(resultId);
            }
        }
    }

    @Nested
    @DisplayName("When the storage contains list samples")
    public class SortedSampleListTest {

        long firstSampleId;
        long secondSampleId;
        long thirdSampleId;

        @BeforeEach
        public void setUp() {
            firstSampleId = useCaseFactory.addSample().addSample(Map.of(
                    Attributes.COLLECTOR_NAME, "Bob",
                    Attributes.PLACE_OF_COLLECTION, "Moscow",
                    Attributes.DATE_OF_COLLECTION, "10-08-2019",
                    Attributes.SAMPLE_TAXONOMY, "Birds"
                    )).sampleId();
            secondSampleId = useCaseFactory.addSample().addSample(Map.of(
                    Attributes.COLLECTOR_NAME, "Rob",
                    Attributes.PLACE_OF_COLLECTION, "Krasnodar",
                    Attributes.DATE_OF_COLLECTION, "01-07-2022",
                    Attributes.SAMPLE_TAXONOMY, "Cats"
            )).sampleId();
            thirdSampleId = useCaseFactory.addSample().addSample(Map.of(
                    Attributes.COLLECTOR_NAME, "Antonio",
                    Attributes.PLACE_OF_COLLECTION, "New York",
                    Attributes.DATE_OF_COLLECTION, "10-10-2020",
                    Attributes.SAMPLE_TAXONOMY, "Animals"
            )).sampleId();
        }

        @ParameterizedTest
        @CsvSource(delimiter='|', value= {"'ALL'|'collectorName'|'asc'",
                                          "'ALL'|'collectorName'|'desc'"})
        public void testSortedSampleListByAscAndDesc(String archiveStatus, String sortedAttribute, String orderedAttribute ) {

            List<Sample> samplesAcs = useCaseFactory.listSamples().listSamples(new SortSampleListParams(
                    archiveStatus,
                    sortedAttribute,
                    orderedAttribute)).samples();

            Assertions.assertThat(samplesAcs.get(0).getSampleId()).isEqualTo(orderedAttribute.equals("asc") ? thirdSampleId : secondSampleId);
            Assertions.assertThat(samplesAcs.get(1).getSampleId()).isEqualTo(firstSampleId);
            Assertions.assertThat(samplesAcs.get(2).getSampleId()).isEqualTo(orderedAttribute.equals("asc") ? secondSampleId : thirdSampleId);
        }
    }
}