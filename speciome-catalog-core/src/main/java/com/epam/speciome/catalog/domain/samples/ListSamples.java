package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.ArchivalStatusException;
import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import com.epam.speciome.catalog.persistence.api.samples.SampleStorage;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ListSamples {
    private final SampleStorage sampleStorage;

    public ListSamples(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Result listSamples() {
        return listSamples(Optional.of(ArchivalStatus.ALL));
    }

    public Result listSamples(Optional<ArchivalStatus> archivalStatus) {
        ArchivalStatus status = archivalStatus.orElse(ArchivalStatus.ALL);

        Map<Long, SampleData> samplesMap = sampleStorage.listSamples().loadSamplesById();
        List<Sample> samples = samplesMap.entrySet().stream()
                .map(entry -> Sample.fromSampleData(entry.getKey(), entry.getValue()))
                .filter(sample -> sampleMatches(sample, status))
                .collect(Collectors.toList());
        return new Result(samples.size(), samples);
    }

    public record Result(int totalCount, List<Sample> samples) {

        public Result(int totalCount, List<Sample> samples) {
            this.totalCount = totalCount;
            this.samples = ImmutableList.copyOf(samples);
        }

        public List<Sample> samples() {
            return ImmutableList.copyOf(samples);
        }
    }

    private boolean sampleMatches(Sample sample, ArchivalStatus archivalStatus) {
        return switch (archivalStatus) {
            case ALL -> true;
            case ARCHIVED -> sample.isArchived();
            case UNARCHIVED -> !sample.isArchived();
        };
    }
}
