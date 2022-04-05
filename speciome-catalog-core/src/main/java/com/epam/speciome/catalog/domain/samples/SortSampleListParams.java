package com.epam.speciome.catalog.domain.samples;


import java.util.Optional;

import static com.epam.speciome.catalog.domain.samples.Attributes.*;

public class SortSampleListParams {

    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc";

    private final Optional<ArchivalStatus> archivalStatusAttribute;
    private final Optional<String> sortedAttribute;
    private final Optional<Boolean> orderedAttribute;

    public SortSampleListParams(String archivalStatus, String sortedAttribute, String orderedAttribute) {
        this.archivalStatusAttribute = ArchivalStatus.defineArchivalStatus(archivalStatus);
        this.sortedAttribute = defineSortedAttribute(sortedAttribute);
        this.orderedAttribute = defineOrderedAttribute(orderedAttribute);
        validateParams();
    }

    public SortSampleListParams(Optional<ArchivalStatus> archivalStatus, Optional<String> sortedAttribute, Optional<Boolean> orderedAttribute) {
        this.archivalStatusAttribute = archivalStatus;
        this.sortedAttribute = sortedAttribute;
        this.orderedAttribute = orderedAttribute;
        validateParams();
    }

    public Optional<ArchivalStatus> getArchivalStatusAttribute() {
        return archivalStatusAttribute;
    }

    public Optional<String> getSortedAttribute() {
        return sortedAttribute;
    }

    public Optional<Boolean> getOrderedAttribute() {
        return orderedAttribute;
    }

    private void validateParams() {
        if (sortedAttribute.isPresent() && orderedAttribute.isEmpty()) throw new SortedAttributeException("'orderby' don't present");
        if (orderedAttribute.isPresent() && sortedAttribute.isEmpty()) throw new SortedAttributeException("'sortby' don't present");
    }

    private Optional<String> defineSortedAttribute(String sortedAttribute) {
        if (sortedAttribute==null) return Optional.empty();
        return switch (sortedAttribute) {
            case COLLECTOR_NAME, PLACE_OF_COLLECTION, DATE_OF_COLLECTION, SAMPLE_TAXONOMY -> Optional.of(sortedAttribute);
            default -> throw new SortedAttributeException(sortedAttribute);
        };
    }

    private Optional<Boolean> defineOrderedAttribute(String orderedAttribute) {
        if (orderedAttribute==null) return Optional.empty();
        return switch (orderedAttribute) {
            case ORDER_ASC -> Optional.of(true);
            case ORDER_DESC -> Optional.of(false);
            default -> throw new SortedAttributeException(orderedAttribute);
        };
    }
}
