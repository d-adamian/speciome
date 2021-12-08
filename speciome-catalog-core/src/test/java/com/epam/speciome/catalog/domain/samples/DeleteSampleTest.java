package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.SampleNotFoundException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
        @DisplayName("Then sample does not exist after I delete it")
        public void testSampleDoesNotExistAfterDeletion() {
            useCaseFactory.deleteSample().deleteSample(sampleId);
            Assertions.assertThatThrownBy(() -> useCaseFactory.getSample().getSample(sampleId))
                    .isInstanceOf(SampleNotFoundException.class);
        }
    }
}
