package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.ImportFileWithMissingColumnsException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Given that CSV file for importing is uploaded")
public class ImportSamplesTest {
    private UseCaseFactory useCaseFactory;

    @BeforeEach
    void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    FileInputStream loadCsvFromResources(String csvName) throws Exception {
        Path csvPath = Paths.get(ClassLoader.getSystemResource(csvName).toURI());
        return new FileInputStream(String.valueOf(csvPath));
    }

    @Nested
    @DisplayName("When empty CSV is imported")
    public class ImportEmptyFile {

        @Test
        @DisplayName("Then error should be thrown")
        public void testImportEmptyFile() throws Exception {

            FileInputStream fileInputStream = loadCsvFromResources("empty_samples.csv");
            assertThrows(ImportFileWithMissingColumnsException.class, () ->
                    useCaseFactory.importSamples().saveSamples(fileInputStream));
        }
    }

    @Nested
    @DisplayName("When there are missing field in CSV header")
    public class ImportWithMissingHeader {

        @Test
        @DisplayName("Then error should be thrown")
        public void testImportWithMissingHeader() throws Exception {

            FileInputStream fileInputStream = loadCsvFromResources("missing_columns.csv");
            assertThrows(ImportFileWithMissingColumnsException.class, () ->
                    useCaseFactory.importSamples().saveSamples(fileInputStream));
        }
    }

    @Nested
    @DisplayName("When CSV with 1 sample is imported")
    public class ImportSingleSample {

        @Test
        @DisplayName("Then one sample from the file will be added to samples storage")
        public void testImportSingleSample() throws Exception {

            FileInputStream fileInputStream = loadCsvFromResources("samples.csv");
            useCaseFactory.importSamples().saveSamples(fileInputStream);
            assertEquals(1, useCaseFactory.listSamples().listSamples().totalCount());
        }
    }

    @Nested
    @DisplayName("When CSV with 4 samples is imported")
    public class ImportSeveralSamples {

        @Test
        @DisplayName("Then all samples from the file will be added to samples storage")
        public void testImportSeveralSamples() throws Exception {

            FileInputStream fileInputStream = loadCsvFromResources("many_samples.csv");
            useCaseFactory.importSamples().saveSamples(fileInputStream);
            assertEquals(4, useCaseFactory.listSamples().listSamples().totalCount());
        }
    }
    @Nested
    @DisplayName("When CSV with excess columns is imported")
    public class ImportExcessColumns {

        @Test
        @DisplayName("Then samples from the file will be successfully added to the storage")
        public void testImportExcessColumns() throws Exception {

            FileInputStream fileInputStream = loadCsvFromResources("excess_columns.csv");
            useCaseFactory.importSamples().saveSamples(fileInputStream);
            List<Sample> samples = useCaseFactory.listSamples().listSamples().samples();
            Map<String, String> importedAttributes = samples.get(0).attributes();

            assertEquals(4, samples.size());

            assertEquals("test name8", importedAttributes.get(Attributes.COLLECTOR_NAME));
            assertEquals("10 Oct", importedAttributes.get(Attributes.DATE_OF_COLLECTION));
            assertEquals("dandelion", importedAttributes.get(Attributes.SAMPLE_TAXONOMY));
            assertEquals("St. Petersburg", importedAttributes.get(Attributes.PLACE_OF_COLLECTION));
        }
    }
}
