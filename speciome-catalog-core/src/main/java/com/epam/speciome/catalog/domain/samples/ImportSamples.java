package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.ImportFileWithMissingColumns;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public final class ImportSamples {
    private final SampleStorage sampleStorage;

    public ImportSamples(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    private List<String[]> readFile(InputStream stream) {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();
        try (Reader reader = new InputStreamReader(stream);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withSkipLines(0)
                     .withCSVParser(parser)
                     .build();
        ) {
            return csvReader.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean areRequiredColumnsPresent(String[] fileAttributes) {
        Set<String> attributesSet = new HashSet<>(Attributes.ALL);
        Set<String> fileAttributesSet = new HashSet<>(Arrays.asList(fileAttributes));
        attributesSet.removeAll(fileAttributesSet);
        return attributesSet.isEmpty();
    }

    private Map<String, Integer> readHeader(List<String[]> lines) {
        HashMap<String, Integer> fileColumns = new HashMap<>();
        Integer i = 0;
        for (String columnName : lines.get(0)) {
            fileColumns.put(columnName, i);
            i++;
        }
        return fileColumns;
    }

    private List<SampleData> readBody(List<String[]> lines, Map<String, Integer> columnIndices) {
        List<SampleData> samples = new ArrayList<>();
        for (String[] line : lines) {
            HashMap<String, String> attributes = new HashMap<>();
            for (Map.Entry<String, Integer> entry : columnIndices.entrySet()) {

                attributes.put(entry.getKey(), line[entry.getValue()]);
            }
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

    private List<SampleData> writeSamples(List<String[]> lines) {
        if (lines.size() == 0 || lines.size() == 1) {
            return Collections.emptyList();
        } else if (areRequiredColumnsPresent(lines.get(0))) {
            Map<String, Integer> fileColumns = readHeader(lines);
            lines.remove(0);
            return readBody(lines, fileColumns);
        } else {
            throw new ImportFileWithMissingColumns();
        }
    }

    public void saveSamples(InputStream stream) {
        List<String[]> list = readFile(stream);
        List<SampleData> data = writeSamples(list);
        for (SampleData sample : data) {
            sampleStorage.addSample(sample);
        }
    }
}
