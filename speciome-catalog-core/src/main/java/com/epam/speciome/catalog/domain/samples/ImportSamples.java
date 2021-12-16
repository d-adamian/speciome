package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.ImportFileWithMissingColumnsException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public final class ImportSamples {
    private final SampleStorage sampleStorage;
    private final RowListProcessor rowListProcessor = new RowListProcessor();

    public ImportSamples(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    private List<String[]> readFile(InputStream stream) throws IOException {
        CsvParser parser = new CsvParser(getParserSettings());
        try(stream) {
            return parser.parseAll(stream);
        }
    }

    private List<SampleData> readBody(List<String[]> lines) {
        List<SampleData> samples = new ArrayList<>();

        for (String[] line : lines) {
            Map<String, String> attributes = getAttributes(line);
            SampleData sampleData = new SampleData(
                    ZonedDateTime.now(ZoneId.of("UTC")),
                    ZonedDateTime.now(ZoneId.of("UTC")),
                    attributes,
                    false
            );
            samples.add(sampleData);
        }
        return samples;
    }

    private Map<String, String> getAttributes(String[] line) {
        Map<String, String> attributes = new HashMap<>();
        int i = 0;
        for (String attributeName : Attributes.ALL) {
            attributes.put(attributeName, line[i]);
            i++;
        }
        return attributes;
    }

    public void saveSamples(InputStream stream) throws IOException {
        List<String[]> list = readFile(stream);
        if (!isAllCollumnsInCSVFile()) {
            throw new ImportFileWithMissingColumnsException();
        }
        List<SampleData> data = readBody(list);
        for (SampleData sample : data) {
            sampleStorage.addSample(sample);
        }
    }

    private boolean isAllCollumnsInCSVFile() {
        String[] headers = rowListProcessor.getHeaders();
        if (headers == null){
            return false;
        }
        return Arrays
                .stream(headers)
                .toList()
                .containsAll(Attributes.ALL);
    }

    private CsvParserSettings getParserSettings() {
        CsvParserSettings settings = new CsvParserSettings();

        settings.setLineSeparatorDetectionEnabled(true);
        settings.setProcessor(rowListProcessor);
        settings.setHeaderExtractionEnabled(true);
        settings.selectFields(Attributes.ALL.toArray(new String[0]));
        settings.setEmptyValue("");
        settings.setNullValue("");
        return settings;
    }
}