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
    private final boolean isDescend;

    public SortCollectionListParams(String sortAttribute, String isDescend) {
        if (isDescend == null)
            throw new SortAttributeException("'orderBy' don't present");
        if (sortAttribute == null)
            throw new SortAttributeException("'sortBy' don't present");
        if (!(isDescend.equalsIgnoreCase(ORDER_ASC) || isDescend.equalsIgnoreCase(ORDER_DESC)))
            throw new SortAttributeException("Incorrect 'orderBy' attribute!");
        if (!ATTRIBUTES.contains(sortAttribute))
            throw new SortAttributeException("Incorrect 'sortBy' attribute!");
        this.sortAttribute = sortAttribute;
        if (isDescend.equalsIgnoreCase(ORDER_DESC)) {
            this.isDescend = true;
        } else {
            this.isDescend = false;
        }

    }

    public String getSortAttribute() {
        return sortAttribute;
    }

    public boolean isDescend() {
        return isDescend;
    }
}