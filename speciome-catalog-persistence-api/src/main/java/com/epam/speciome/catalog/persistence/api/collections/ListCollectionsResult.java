package com.epam.speciome.catalog.persistence.api.collections;

import java.util.List;
import java.util.Map;

public class ListCollectionsResult {

    private final int totalCount;
    private final Map<Long, CollectionData> collectionDataMap;
    private List<Long> orderList = null;

    public ListCollectionsResult(int totalCount, Map<Long, CollectionData> collectionDataMap) {
        this.totalCount = totalCount;
        this.collectionDataMap = Map.copyOf(collectionDataMap);
    }
    public ListCollectionsResult(int totalCount, Map<Long, CollectionData> collectionDataMap, List<Long> orderList) {
        this.totalCount = totalCount;
        this.collectionDataMap = Map.copyOf(collectionDataMap);
        this.orderList = orderList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public Map<Long, CollectionData> getCollectionDataMap() {
        return Map.copyOf(collectionDataMap);
    }

    public List<Long> getOrderList() {
        return orderList;
    }

}
