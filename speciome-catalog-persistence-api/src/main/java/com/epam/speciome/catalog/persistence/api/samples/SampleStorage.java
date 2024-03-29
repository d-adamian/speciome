package com.epam.speciome.catalog.persistence.api.samples;

import com.epam.speciome.catalog.persistence.api.exceptions.SampleIsNullException;

import java.util.List;
import java.util.Optional;

public interface SampleStorage {
    Long addSample(SampleData sampleData);

    ListSamplesResult listSamples();

    ListSamplesResult listSamplesSortedByParams(String sortBy, boolean orderBy);

    /**
     *
     * @throws SampleIsNullException
     * if a Sample with the given id cannot be found
     */
    SampleData getSampleById(Long sampleId);

    void deleteSampleById(Long sampleId);

    void updateSample(Long sampleId, SampleData sampleData);

    List<Long> addMultipleSamples(List<SampleData> samples);

}
