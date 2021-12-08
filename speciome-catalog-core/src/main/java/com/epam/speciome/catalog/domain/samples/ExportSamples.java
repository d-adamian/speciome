package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import com.opencsv.CSVWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

        Map<Long, SampleData> sampleDataMap = sampleStorage.listSamples().loadSamplesById();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (var outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             CSVWriter writer = new CSVWriter(outputStreamWriter)) {

            writer.writeNext(writeHeader().toArray(String[]::new));

            for (Map.Entry<Long, SampleData> entry : sampleDataMap.entrySet()) {
                SampleData sampleData = entry.getValue();
                List<String> sampleDataRow = new ArrayList<>();
                sampleDataRow.add(String.valueOf(entry.getKey()));
                sampleDataRow.add(String.valueOf(sampleData.createdAt()));
                sampleDataRow.add(String.valueOf(sampleData.updatedAt()));
                for (String attribute : Attributes.ALL) {
                    sampleDataRow.add(sampleData.attributes().get(attribute));
                }
                String[] sampleArray = sampleDataRow.toArray(String[]::new);
                writer.writeNext(sampleArray);
            }
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private List<String> writeHeader() {
        List<String> header = new ArrayList<>();
        header.add("Id");
        header.add("Created At");
        header.add("Updated At");
        header.addAll(Attributes.ALL);
        return header;
    }
}