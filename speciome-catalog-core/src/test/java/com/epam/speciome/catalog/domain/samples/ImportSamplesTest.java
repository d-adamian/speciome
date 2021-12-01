package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        @DisplayName("Then nothing will be added to samples storage")
        public void testImportEmptyFile() throws Exception {

            FileInputStream fileInputStream = loadCsvFromResources("empty_samples.csv");
            useCaseFactory.importSamples().saveSamples(fileInputStream);
            assertEquals(Collections.emptyList(), useCaseFactory.listSamples().listSamples().getSamples());
        }
    }

    @Nested
    @DisplayName("When CSV with missing columns is imported")
    public class ImportMissingColumns {

        @Test
        @DisplayName("Then error is thrown")
        public void testImportMissingColumns() throws Exception {

            FileInputStream fileInputStream = loadCsvFromResources("missing_columns.csv");
            Assertions.assertThrows(ImportFileWithMissingColumns.class, () -> {
                useCaseFactory.importSamples().saveSamples(fileInputStream);
            });
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
            assertEquals(1, useCaseFactory.listSamples().listSamples().getTotalCount());
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
            assertEquals(4, useCaseFactory.listSamples().listSamples().getTotalCount());
        }
    }
}
