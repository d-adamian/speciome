package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayName("Given that sample collection is empty")
class ExportSamplesTest {

    private UseCaseFactory useCaseFactory;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
        out = new ByteArrayOutputStream();
    }

    @Nested
    @DisplayName("When export method is called on an empty storage")
    public class ExportEmptyStorage {

        private final String[] columnNames = {"Id", Attributes.COLLECTOR_NAME,
                Attributes.PLACE_OF_COLLECTION, Attributes.DATE_OF_COLLECTION, Attributes.SAMPLE_TAXONOMY};

        @Test
        @DisplayName("Then just the header is written by CSVWriter")
        public void testExportEmptyStorage() throws IOException {
            useCaseFactory.exportSamples().exportSamples().transferTo(out);
            String[] getContentFromCSV = out.toString(StandardCharsets.UTF_8).trim().split(",+");
            assertArrayEquals(columnNames, getContentFromCSV);
        }
    }
    @Nested
    @DisplayName("When one sample with empty attributes has been added")
    public class SamplesWithEmptyAttributesAdded {

        @BeforeEach
        public void setUp() throws IOException {
            useCaseFactory.addSample().addSampleWithoutAttributes();
            useCaseFactory.exportSamples().exportSamples().transferTo(out);
        }

        @Test
        void addOneEmptySampleField() {
            String output = out.toString(StandardCharsets.UTF_8);
            List<String> outputList = output.lines().toList();
            Assertions.assertThat(outputList.get(1)).isEqualTo(getSampleAttributesByID(0));
        }
    }
    @Nested
    @DisplayName("When sample with filled attributes from 1 till 4")
    public class SamplesWithAttributesAdded {

        @BeforeEach
        public void setUp() throws IOException {
            Map<String, String> mapToTestAttributes = Map.ofEntries(
                    Map.entry(Attributes.SAMPLE_TAXONOMY, "mammal"),
                    Map.entry(Attributes.DATE_OF_COLLECTION, "2021-10-21"),
                    Map.entry(Attributes.PLACE_OF_COLLECTION, "Tbilisi"),
                    Map.entry(Attributes.COLLECTOR_NAME, "Pushkin"));

            useCaseFactory.addSample().addSample(mapToTestAttributes);
            useCaseFactory.exportSamples().exportSamples().transferTo(out);
        }

        @Test
        @DisplayName("If the header is ok all the elements should be outputted")
        void allAttributesFilled() {

            String output = out.toString(StandardCharsets.UTF_8);
            List<String> outputList = output.lines().toList();
            Assertions.assertThat(outputList.get(1)).isEqualTo(getSampleAttributesByID(0));

        } // end dependentAssertions

    } // end Samples with Empty attributes

    private String getSampleAttributesByID(int id) {
        Sample sample = useCaseFactory.listSamples().listSamples().samples().get(id);
        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner.add(sample.sampleId().toString());

        for (String attribute : Attributes.ALL) {
            stringJoiner.add(sample.attributes().get(attribute));
        }
        return stringJoiner.toString();
    }
}