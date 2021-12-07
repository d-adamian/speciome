package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        private final String[] columnNames = {"Id", "Created At", "Updated At", Attributes.COLLECTOR_NAME,
                Attributes.PLACE_OF_COLLECTION, Attributes.DATE_OF_COLLECTION, Attributes.SAMPLE_TAXONOMY};

        @Test
        @DisplayName("Then just the header is written by CSVWriter")
        public void testExportEmptyStorage() throws IOException {
            useCaseFactory.exportSamples().exportSamples().transferTo(out);
            CSVReader reader = new CSVReader(new StringReader(out.toString()));
            String[] headerResult = reader.readNext();
            int count = 0;
            while (reader.readNext() != null) {
                reader.readNext();
                count++;
            }
            reader.close();
            assertArrayEquals(columnNames, headerResult);
            assertEquals(0, count);
        }
    }

    @Nested
    @DisplayName("When the storage contains a single sample")
    public class ExportSingleSample {

        @BeforeEach
        public void setUp() {
            Map<String, String> sampleAttributes = new HashMap<>();
            sampleAttributes.put(Attributes.COLLECTOR_NAME, "Mary Jones");
            sampleAttributes.put(Attributes.DATE_OF_COLLECTION, "01.01.01");
            sampleAttributes.put(Attributes.PLACE_OF_COLLECTION, "St. Petersburg");
            sampleAttributes.put(Attributes.SAMPLE_TAXONOMY, "Dandelion");

            useCaseFactory.addSample().addSample(sampleAttributes);
        }

        @Test
        @DisplayName("Then just that sample is written by CSVWriter")
        public void testExportSingleEntry() throws IOException {
            useCaseFactory.exportSamples().exportSamples().transferTo(out);

            CSVReader reader = new CSVReader(new StringReader(out.toString()));

            @SuppressWarnings("unused") // The variable name is for explanatory purposes
            String[] headers = reader.readNext();

            String[] sampleLine;
            List<String> samplesCount = new ArrayList<>();
            while ((sampleLine = reader.readNext()) != null) {
                samplesCount.add(sampleLine[6]);
            }
            reader.close();
            assertEquals(1, samplesCount.size());
            assertEquals("Dandelion", samplesCount.get(0));
        }
    }
}