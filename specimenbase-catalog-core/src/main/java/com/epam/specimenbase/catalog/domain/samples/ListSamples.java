package com.epam.specimenbase.catalog.domain.samples;

import com.epam.specimenbase.catalog.ports.ListSamplesResult;
import com.epam.specimenbase.catalog.ports.SampleStorage;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

public final class ListSamples {
    private final SampleStorage sampleStorage;

    public ListSamples(SampleStorage sampleStorage) {
        this.sampleStorage = sampleStorage;
    }

    public Result listSamples() {
        ListSamplesResult listResult = sampleStorage.listSamples();
        List<Sample> samples = listResult.getSamplesById().entrySet().stream()
                .map(entry -> Sample.fromSampleData(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return new Result(listResult.getTotalCount(), samples);
    }

    public static final class Result {
        private final int totalCount;
        private final List<Sample> samples;

        private Result(int totalCount, List<Sample> samples) {
            this.totalCount = totalCount;
            this.samples = ImmutableList.copyOf(samples);
        }

        public int getTotalCount() {
            return totalCount;
        }

        public List<Sample> getSamples() {
            return ImmutableList.copyOf(samples);
        }
    }
}
