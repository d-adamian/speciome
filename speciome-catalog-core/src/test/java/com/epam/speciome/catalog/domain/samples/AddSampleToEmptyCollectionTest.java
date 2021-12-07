package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@DisplayName("Given that sample collection is empty")
public class AddSampleToEmptyCollectionTest {
    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    @Nested
    @DisplayName("When no samples have been added")
    public class NoSamplesAdded {

        @Test
        @DisplayName("Then there are no samples in collection")
        public void testCollectionIsEmpty() {
            ListSamples.Result listResult = useCaseFactory.listSamples().listSamples();
            Assertions.assertThat(listResult).isNotNull();

            int samplesTotal = listResult.getTotalCount();
            Assertions.assertThat(samplesTotal).isEqualTo(0);

            List<Sample> samples = listResult.getSamples();
            Assertions.assertThat(samples).isEmpty();
        }
    }

    @Nested
    @DisplayName("When one sample with empty attributes has been added")
    public class OneSampleWithEmptyAttributesAdded {
        private Long assignedSampleId = null;
        private ZonedDateTime creationTime = null;

        @BeforeEach
        public void setUp() {
            AddSample addSample = useCaseFactory.addSample();
            AddSample.Result result = addSample.addSampleWithoutAttributes();
            assignedSampleId = result.getSampleId();
            creationTime = ZonedDateTime.now(ZoneId.of("UTC"));
        }

        @Test
        @DisplayName("Then collection contains one sample")
        public void testCollectionHasOneSample() {
            ListSamples.Result list = useCaseFactory.listSamples().listSamples();
            Assertions.assertThat(list.getTotalCount()).isEqualTo(1);
            List<Sample> samples = list.getSamples();
            Assertions.assertThat(samples).isNotNull();
            Assertions.assertThat(samples.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("Then single sample has non-empty identifier equal to assigned identifier")
        public void testSampleHasNonEmptyIdentifier() {
            Sample sample = getFirstSample();
            Long sampleId = sample.getSampleId();
            Assertions.assertThat(sampleId).isNotNull();
            Assertions.assertThat(sampleId).isEqualTo(assignedSampleId);
        }

        @Test
        @DisplayName("Then sample can be retrieved by assigned identifier")
        public void testSampleCanBeRetrievedBySampleId() {
            GetSample.Result result = useCaseFactory.getSample().getSample(assignedSampleId);

            Long sampleId = result.getSampleId();

            Assertions.assertThat(sampleId).isNotNull().isEqualTo(assignedSampleId);

            Sample sampleFromList = getFirstSample();
            Sample sampleRetrieved = result.getSample();
            Assertions.assertThat(sampleRetrieved).isNotNull().isEqualTo(sampleFromList);
        }

        @Test
        @DisplayName("Then no sample can be retrieved by other identifier")
        public void testNoOtherSampleCanBeRetrieved() {
            Assertions.assertThatThrownBy(() -> useCaseFactory.getSample().getSample(536363654L))
                    .isInstanceOf(SampleNotFoundException.class);
        }

        @Test
        @DisplayName("Then sample has created time and updated time close to time of its creation")
        public void testSampleCreatedAndUpdateTimeIsCorrect() {
            Sample sample = getFirstSample();

            TemporalUnitWithinOffset tolerance = new TemporalUnitWithinOffset(5, ChronoUnit.SECONDS);

            ZonedDateTime createdAt = sample.getCreatedAt();
            ZonedDateTime updatedAt = sample.getUpdatedAt();
            Assertions.assertThat(creationTime)
                    .isNotNull()
                    .isCloseTo(createdAt, tolerance)
                    .isCloseTo(updatedAt, tolerance);
        }

        @Test
        @DisplayName("Then sample has mandatory attributes")
        public void testSampleHasAttributes() {
            Map<String, String> attributes = getFirstSample().getAttributes();
            Assertions.assertThat(attributes.keySet())
                    .containsExactlyInAnyOrderElementsOf(Attributes.ALL);
        }

        @Test
        @DisplayName("Then all attributes are empty")
        public void testAllAttributesAreEmpty() {
            Map<String, String> attributes = getFirstSample().getAttributes();
            Assertions.assertThat(attributes.values()).containsOnly("");
        }
    }

    @Nested
    @DisplayName("When one sample with non-empty attributes has been added")
    public class OneSampleWithAllAttributesAdded {
        @SuppressWarnings("UnstableApiUsage")
        private final Map<String, String> attributes = Attributes.ALL.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        attr -> Hashing.murmur3_32().hashString(attr, Charset.defaultCharset()).toString()
                ));

        @Test
        @DisplayName("Then attribute values are equal to given values")
        public void testAttributesAreEqualToProvidedValues() {
            // Using sample ID here to keep mutational testing happy
            Long sampleId = useCaseFactory.addSample().addSample(attributes).getSampleId();
            Sample sample = useCaseFactory.getSample().getSample(sampleId).getSample();

            Map<String, String> sampleAttributes = sample.getAttributes();
            Assertions.assertThat(sampleAttributes).containsExactlyEntriesOf(attributes);
        }
    }

    @Nested
    @DisplayName("When one sample with partial list of attributes has been added")
    public class OneSampleWithPartialAttributesAdded {
        private final Map<String, String> attributes = Map.of(
                Attributes.ALL.get(0), "Mock_value_1",
                Attributes.ALL.get(1), "Mock_value_2"
        );

        @Test
        @DisplayName("Then attributes are empty for non-provided values")
        public void testAttributesAreEmptyForNonProvidedValues() {
            useCaseFactory.addSample().addSample(attributes);
            Map<String, String> sampleAttributes = getFirstSample().getAttributes();

            Sets.SetView<String> expectedEmptyAttributes = Sets.difference(
                    Set.copyOf(Attributes.ALL), attributes.keySet()
            );
            for (String attribute: expectedEmptyAttributes) {
                Assertions.assertThat(sampleAttributes).containsEntry(attribute, "");
            }
        }
    }

    @Nested
    @DisplayName("When one sample with extra attributes is added")
    public class OneSampleWithExtraAttributes {
        private final Map<String, String> attributes = Map.of("UNKNOWN_ATTRIBUTE", "234");

        @Test
        @DisplayName("Then exception is thrown that attribute is unknown")
        public void testExceptionIsThrown() {
            Assertions.assertThatThrownBy(() -> useCaseFactory.addSample().addSample(attributes))
                    .isInstanceOf(UnexpectedAttributeException.class);
        }
    }

    @Nested
    @DisplayName("When two samples have been added")
    public class TwoSamplesAdded {
        private Long sampleIdOne;
        private Long sampleIdTwo;

        @BeforeEach
        void setUp() {
            sampleIdOne = useCaseFactory.addSample().addSampleWithoutAttributes().getSampleId();
            sampleIdTwo = useCaseFactory.addSample().addSampleWithoutAttributes().getSampleId();
        }

        @Test
        @DisplayName("Then samples have different identifiers")
        public void testSamplesHaveDifferentIdentifiers() {
            Assertions.assertThat(sampleIdOne).isNotEqualTo(sampleIdTwo);
        }

        @Test
        @DisplayName("Then both samples are present")
        public void testTwoSamplesArePresent() {
            Assertions.assertThat(useCaseFactory.listSamples().listSamples().getSamples()).hasSize(2);
            for (Long sampleId : List.of(sampleIdOne, sampleIdTwo)) {
                GetSample.Result getResult = useCaseFactory.getSample().getSample(sampleId);
                Assertions.assertThat(getResult.getSampleId()).isEqualTo(sampleId);
                Assertions.assertThat(getResult.getSample().getSampleId()).isEqualTo(sampleId);
            }
        }
    }

    private Sample getFirstSample() {
        ListSamples.Result list = useCaseFactory.listSamples().listSamples();
        Assertions.assertThat(list.getSamples()).isNotNull().isNotEmpty();
        return list.getSamples().get(0);
    }
}
