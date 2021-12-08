package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnArchiveSampleTest {

    private UseCaseFactory useCaseFactory;
    private static final int NUM_SAMPLES = 3;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
        for (int i = 0; i < NUM_SAMPLES; i++) {
            useCaseFactory.addSample().addSampleWithoutAttributes();
            useCaseFactory.archiveSample().archiveSample((long) i);
        }
    }

    @Test
    @DisplayName("Should throw SampleNotFoundException when Sample not found")
    public void throwsExceptionWhenNotFound() {
        assertThrows(SampleNotFoundException.class, () -> useCaseFactory.unArchiveSample().unArchiveSample(33L));
    }

    @Test
    @DisplayName("Should return not Null")
    public void returnsNotNull() {
        assertNotNull(useCaseFactory.unArchiveSample().unArchiveSample(0L));
    }

    @Test
    @DisplayName("Sample Ids should be the same")
    public void eqalsId() {
        assertEquals(0L, useCaseFactory.unArchiveSample().unArchiveSample(0L).sampleId());
    }

    @Test
    @DisplayName("archived field is true before archiving")
    public void testsThatSampleIsArchived() {
        assertTrue(useCaseFactory.getSample().getSample(0L).sample().isArchived());
    }

    @Nested
    @DisplayName("Test unArchiving different Samples")
    public class UnArchivingSamples {

        @Test
        @DisplayName("UnArchive zeroth sample")
        public void testZerothElement() {
            useCaseFactory.unArchiveSample().unArchiveSample(0L);
            assertFalse(useCaseFactory.getSample().getSample(0L).sample().isArchived());
        }

        @Test
        @DisplayName("UnArchive all samples")
        public void testAllElements() {
            assertAll("archived field is true before archiving",
                    () -> {
                        assertAll(
                                () -> assertTrue(useCaseFactory.getSample().getSample(0L).sample().isArchived()),
                                () -> assertTrue(useCaseFactory.getSample().getSample(1L).sample().isArchived()),
                                () -> assertTrue(useCaseFactory.getSample().getSample(2L).sample().isArchived())
                        );

                        for (int i = 0; i < NUM_SAMPLES; i++) {
                            useCaseFactory.unArchiveSample().unArchiveSample((long) i);
                        }

                        assertAll("archived field change to false",
                                () -> assertFalse(useCaseFactory.getSample().getSample(0L).sample().isArchived()),
                                () -> assertFalse(useCaseFactory.getSample().getSample(1L).sample().isArchived()),
                                () -> assertFalse(useCaseFactory.getSample().getSample(2L).sample().isArchived())
                        );
                    }
            );
        }
    }
}