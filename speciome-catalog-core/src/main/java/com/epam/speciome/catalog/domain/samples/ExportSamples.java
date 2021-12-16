package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ExportSamples {
    private final SampleStorage sampleStorage;

    public ExportSamples(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public ByteArrayInputStream exportSamples() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (Writer outputStreamWriter = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {

            CsvWriter writer = new CsvWriter(outputStreamWriter, new CsvWriterSettings());
            writer.writeHeaders(getHeaderColumns());
            writer.writeRowsAndClose(getTableRows());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private List<List<Object>> getTableRows() {
        List<List<Object>> listToOutputInCSV = new ArrayList<>();
        Map<Long, SampleData> sampleDataMap = sampleStorage.listSamples().loadSamplesById();

        for (Map.Entry<Long, SampleData> entry : sampleDataMap.entrySet()) {
            List<Object> sampleDataRow = new ArrayList<>();

            SampleData value = entry.getValue();

            sampleDataRow.add(entry.getKey());
            for (String attribute : Attributes.ALL) {
                sampleDataRow.add(value.attributes().get(attribute));
            }

            listToOutputInCSV.add(sampleDataRow);
        }
        return listToOutputInCSV;
    }

    private List<String> getHeaderColumns() {
        List<String> header = new ArrayList<>();
        header.add("Id");
        header.addAll(Attributes.ALL);
        return header;
    }
}