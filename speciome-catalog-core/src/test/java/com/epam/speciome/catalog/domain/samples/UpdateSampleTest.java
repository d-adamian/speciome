package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

@DisplayName("Given that sample collection is empty")
public class UpdateSampleTest {
    private UseCaseFactory useCaseFactory;

    @BeforeEach
    void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    @Nested
    @DisplayName("When I try to update sample in empty collection")
    public class UpdateInEmptyCollection {

        @Test
        @DisplayName("Then exception is thrown")
        public void testExceptionIsThrown() {
            Long sampleId = 1534534645L;
            Map<String, String> attributes = Map.of();
            Assertions.assertThatThrownBy(() -> useCaseFactory.updateSample().updateSample(sampleId, attributes))
                    .isInstanceOf(SampleNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("When I update already existing sample")
    public class UpdateExistingSample {
        private final Map<String, String> attributes = Map.of(
                Attributes.ALL.get(0), "Mock_value_1",
                Attributes.ALL.get(1), "Mock_value_2"
        );

        private Long sampleId;
        private Sample originalSample;
        private Sample updatedSample;

        @BeforeEach
        void setUp() {
            sampleId = useCaseFactory.addSample().addNewSampleWithoutAttributes().getSampleId();
            originalSample = useCaseFactory.getSample().getSample(sampleId).getSample();

            updatedSample = useCaseFactory.updateSample().updateSample(sampleId, attributes).getSample();
        }

        @Test
        @DisplayName("Then sample attributes are equal to given values")
        public void testAttributesAreSet() {
            Assertions.assertThat(updatedSample.getAttributes()).containsAllEntriesOf(attributes);
        }

        @Test
        @DisplayName("Then sample update date is increased")
        public void testUpdateDateIsIncreased() {
            Assertions.assertThat(updatedSample.getUpdatedAt()).isAfter(originalSample.getUpdatedAt());
        }

        @Test
        @DisplayName("Then sample creation date is unchanged")
        public void testCreationDateIsUnchanged() {
            Assertions.assertThat(originalSample.getCreatedAt()).isEqualTo(updatedSample.getCreatedAt());
        }

        @Test
        @DisplayName("Then returned sample is identical to sample in storage")
        public void testReturnedSampleSameAsGetNext() {
            GetSample.Result getResult = useCaseFactory.getSample().getSample(sampleId);
            Assertions.assertThat(getResult.getSampleId()).isEqualTo(sampleId);
            Assertions.assertThat(getResult.getSample()).isEqualTo(updatedSample);
        }
    }

    @Nested
    @DisplayName("When I update existing sample adding incorrect attributes")
    public class UpdateExistingSampleWithIncorrectAttributes {

        private Long sampleId;

        @BeforeEach
        void setUp() {
            sampleId = useCaseFactory.addSample().addNewSampleWithoutAttributes().getSampleId();
        }

        @Test
        @DisplayName("Then exception is thrown")
        void testIncorrectAttributes() {
            Map<String, String> attributes = Map.of("INCORRECT_ATTRIBUTE", "SOME_VALUE");
            Assertions.assertThatThrownBy(
                    () -> useCaseFactory.updateSample().updateSample(sampleId, attributes)
            ).isInstanceOf(UnexpectedAttributeException.class);
        }
    }
}
