package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArchiveSampleTest {

    private UseCaseFactory useCaseFactory;
    private static final int NUM_SAMPLES = 3;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
        for (int i = 0; i < NUM_SAMPLES; i++) {
            useCaseFactory.addSample().addSampleWithoutAttributes();
        }
    }

    @Test
    @DisplayName("Field archive should be false after creation")
    public void archiveSampleFalseWhenCreated() {
        assertAll(
                () -> assertFalse(useCaseFactory.getSample().getSample(0L).getSample().isArchived()),
                () -> assertFalse(useCaseFactory.getSample().getSample(1L).getSample().isArchived()),
                () -> assertFalse(useCaseFactory.getSample().getSample(2L).getSample().isArchived())
        );
    }

    @Test
    @DisplayName("Should throw SampleNotFoundException when Sample not found")
    public void throwsExceptionWhenNotFound() {
        assertThrows(SampleNotFoundException.class, () -> useCaseFactory.archiveSample().archiveSample(33L));
    }

    @Test
    @DisplayName("Should return not Null")
    public void returnsNotNull() {
       assertNotNull(useCaseFactory.archiveSample().archiveSample(0L));
    }

    @Test
    @DisplayName("Sample Ids should be the same")
    public void eqalsId() {
        assertEquals(0L, useCaseFactory.archiveSample().archiveSample(0L).getSampleId());
    }

    @Nested
    @DisplayName("Test archiving different Samples")
    public class ArchivingSamples {
        @BeforeEach
        public void setUp() {
            for (int i = 0; i < NUM_SAMPLES; i++) {
                useCaseFactory.archiveSample().archiveSample((long) i);
            }
        }

        @Test
        @DisplayName("Archive zeroth sample")
        public void testZerothElement() {
            assertTrue(useCaseFactory.getSample().getSample(0L).getSample().isArchived());
        }
        @Test
        @DisplayName("Archive first sample")
        public void testFirstElement() {
            assertTrue(useCaseFactory.getSample().getSample(1L).getSample().isArchived());
        }
        @Test
        @DisplayName("Archive all elements test")
        public void testAllThreeElements() {
            assertAll(
                    () -> assertTrue(useCaseFactory.getSample().getSample(0L).getSample().isArchived()),
                    () -> assertTrue(useCaseFactory.getSample().getSample(1L).getSample().isArchived()),
                    () -> assertTrue(useCaseFactory.getSample().getSample(2L).getSample().isArchived())
            );
        }
    }
}