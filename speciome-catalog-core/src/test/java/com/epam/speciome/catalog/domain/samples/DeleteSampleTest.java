package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.SampleNotArchivedException;
import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Given that sample collection is empty")
public class DeleteSampleTest {
    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    @Nested
    @DisplayName("When I try to delete sample")
    public class DeleteFromEmptyCollection {

        @Test
        @DisplayName("Then exception is thrown")
        public void testExceptionIsThrown() {
            Long sampleId = 12343534L;
            Assertions.assertThatThrownBy(() -> useCaseFactory.deleteSample().deleteSample(sampleId))
                    .isInstanceOf(SampleNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("When I have created a sample")
    public class DeleteCreatedSample {
        Long sampleId;

        @BeforeEach
        public void setUp() {
            sampleId = useCaseFactory.addSample().addSampleWithoutAttributes().sampleId();
        }

        @Test
        @DisplayName("Then throw exception if it is not archived")
        public void testThrowExceptionIfNotArchived() {
            assertThrows(SampleNotArchivedException.class, () ->
                    useCaseFactory.deleteSample().deleteSample(sampleId));
        }

        @Test
        @DisplayName("When element is deleted it then list of Samples is empty")
        public void testSuccessfulDeletion() {
           useCaseFactory.archiveSample().archiveSample(sampleId);
           useCaseFactory.deleteSample().deleteSample(sampleId);
           assertTrue(useCaseFactory.listSamples().listSamples().samples().isEmpty());
        }
    }
}
