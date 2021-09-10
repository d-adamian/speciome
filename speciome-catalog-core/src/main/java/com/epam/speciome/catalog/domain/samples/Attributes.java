package com.epam.speciome.catalog.domain.samples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Attributes {
    String COLLECTOR_NAME = "collectorName";
    String PLACE_OF_COLLECTION = "placeOfCollection";
    String DATE_OF_COLLECTION = "dateOfCollection";
    String SAMPLE_TAXONOMY = "sampleTaxonomy";

    List<String> ALL = List.of(COLLECTOR_NAME, PLACE_OF_COLLECTION, DATE_OF_COLLECTION, SAMPLE_TAXONOMY);

    static void checkExtraAttributes(Map<String, String> inputAttributes) {
        for (String attribute: inputAttributes.keySet()) {
            if (!ALL.contains(attribute)) {
                throw new UnexpectedAttributeException(attribute);
            }
        }
    }

    static Map<String, String> fillMissingAttributeValues(Map<String, String> inputAttributes) {
        Map<String, String> attributes = new HashMap<>(inputAttributes);
        for (String attribute: ALL) {
            attributes.putIfAbsent(attribute, "");
        }
        return attributes;
    }

}
