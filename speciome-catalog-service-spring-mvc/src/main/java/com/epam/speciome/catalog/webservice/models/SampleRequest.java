package com.epam.speciome.catalog.webservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SampleRequest {
    private final List<SampleAttribute> attributes;

    @JsonCreator
    public SampleRequest(@JsonProperty("attributes") List<SampleAttribute> attributes) {
        this.attributes = List.copyOf(attributes);
    }

    @JsonProperty
    public List<SampleAttribute> getAttributes() {
        return List.copyOf(attributes);
    }
}
