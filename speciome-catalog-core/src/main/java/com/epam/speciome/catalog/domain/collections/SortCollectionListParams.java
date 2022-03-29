package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.SortAttributeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortCollectionListParams {
    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc";
    public static final List<String> ATTRIBUTES =
            new ArrayList<>(Arrays.asList("id", "collectionName", "createdAtUtc", "updatedAtUtc", "ownerEmail", "archived"));
    private final String sortAttribute;
    private final boolean isDecrease;

    public SortCollectionListParams(String sortAttribute, String isDecrease) {
        if (isDecrease == null || !(isDecrease.equalsIgnoreCase(ORDER_ASC) || isDecrease.equalsIgnoreCase(ORDER_DESC)))
            throw new SortAttributeException("'orderBy' don't present");
        if (sortAttribute == null || !ATTRIBUTES.contains(sortAttribute))
            throw new SortAttributeException("'sortBy' don't present");
        this.sortAttribute = sortAttribute;
        if (isDecrease.equalsIgnoreCase(ORDER_DESC)) {
            this.isDecrease = true;
        } else {
            this.isDecrease = false;
        }

    }

    public String getSortAttribute() {
        return sortAttribute;
    }

    public boolean isDecrease() {
        return isDecrease;
    }
}