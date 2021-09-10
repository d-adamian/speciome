package com.epam.speciome.catalog.webservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public final class SampleAttribute {
    private final String attribute;
    private final String value;

    @JsonCreator
    public SampleAttribute(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("value") String value
    ) {
        this.attribute = attribute == null ? "" : attribute;
        this.value = value == null ? "" : value;
    }

    @Schema(description = "Attribute name", example = "collectorName")
    @JsonProperty(required = true)
    public String getAttribute() {
        return attribute;
    }

    @Schema(description = "Attribute value", example = "ABC")
    @JsonProperty(required = true)
    public String getValue() {
        return value;
    }
}
