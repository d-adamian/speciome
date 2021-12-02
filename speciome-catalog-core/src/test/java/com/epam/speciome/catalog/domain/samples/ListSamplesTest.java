package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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
            int samplesCount = listResult.getTotalCount();
            Assertions.assertThat(samplesCount).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("When the storage contains a single unarchived sample")
    public class ListSingleUnarchivedSample {

        long sampleId;

        @BeforeEach
        public void setUp() {
            sampleId = useCaseFactory.addSample().addSampleWithoutAttributes().getSampleId();
        }

        @ParameterizedTest
        @EnumSource(ArchivalStatus.class)
        public void testCaseUnarchivedSample(ArchivalStatus archivalStatus) {
            ListSamples.Result listResult =
                    useCaseFactory.listSamples().listSamples(Optional.of(archivalStatus));
            int samplesCount = listResult.getTotalCount();
            if (archivalStatus.equals(ArchivalStatus.ARCHIVED)) {
                Assertions.assertThat(samplesCount).isEqualTo(0);
            } else if (archivalStatus.equals(ArchivalStatus.ALL) || archivalStatus.equals(ArchivalStatus.UNARCHIVED)){
                long resultId = listResult.getSamples().get(0).getSampleId();
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
            sampleId = useCaseFactory.addSample().addSampleWithoutAttributes().getSampleId();
            useCaseFactory.archiveSample().archiveSample(sampleId);
        }

        @ParameterizedTest
        @EnumSource(ArchivalStatus.class)
        public void testCaseArchivedSample(ArchivalStatus archivalStatus) {
            ListSamples.Result listResult =
                    useCaseFactory.listSamples().listSamples(Optional.of(archivalStatus));
            int samplesCount = listResult.getTotalCount();
            if (archivalStatus.equals(ArchivalStatus.UNARCHIVED)) {
                Assertions.assertThat(samplesCount).isEqualTo(0);
            } else if (archivalStatus.equals(ArchivalStatus.ALL) || archivalStatus.equals(ArchivalStatus.ARCHIVED)) {
                long resultId = listResult.getSamples().get(0).getSampleId();
                Assertions.assertThat(samplesCount).isEqualTo(1);
                Assertions.assertThat(sampleId).isEqualTo(resultId);
            }
        }
    }
}