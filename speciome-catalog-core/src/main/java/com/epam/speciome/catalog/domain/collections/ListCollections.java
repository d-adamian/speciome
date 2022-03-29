package com.epam.speciome.catalog.domain.collections;

import com.epam.speciome.catalog.domain.exceptions.SortAttributeException;
import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.api.collections.CollectionStorage;
import com.epam.speciome.catalog.persistence.api.collections.ListCollectionsResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ListCollections {
    private final CollectionStorage collectionStorage;
//    private final static List<String> values = Arrays.asList("collectionName", "createdAtUtc", "updatedAtUtc", "ownerEmail");

    public ListCollections(CollectionStorage collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public List<Collection> listCollections(String sortBy, String orderBy) {
        boolean withParams = false;
        if(sortBy!=null && orderBy!=null) withParams = true;
//
//        ListCollectionsResult listCollectionsResult;
//
//        SortCollectionListParams params = new SortCollectionListParams(sortBy, orderBy);
//
//        if (values.contains(sortBy)) {
//            listCollectionsResult = collectionStorage.sortedListCollections(sortBy, false);
//
//        } else {
//            listCollectionsResult = collectionStorage.listCollections();
//        }
//
//        List<Collection> result = new ArrayList<>();
//
//        List<Long> order = listCollectionsResult.getOrder();
//
//        Map<Long, CollectionData> map = listCollectionsResult.getCollectionDataMap();
//
//        if (order != null) {
//            for (int i = 0; i < order.size(); i++) {
//                long tmp = order.get(i);
//                result.add(Collection.fromCollectionData(tmp, map.get(tmp)));
//            }
//            return result;
//        } else {
//            return listCollectionsResult.getCollectionDataMap()
//                    .entrySet()
//                    .stream()
//                    .map(entry -> Collection.fromCollectionData(entry.getKey(), entry.getValue()))
//                    .collect(Collectors.toList());
//                    }
        try {
            return getResult(withParams, sortBy, orderBy);
        }catch (SortAttributeException e) {throw new SortAttributeException(e.getMessage());}
    }



    public List<Collection> getResult(boolean withParams, String sortBy, String orderBy) {

        ListCollectionsResult listCollectionsResult;

        List<Collection> result = new ArrayList<>();

        if (withParams) {
            SortCollectionListParams params = new SortCollectionListParams(sortBy, orderBy);

            listCollectionsResult = collectionStorage.sortedListCollections(params.getSortAttribute(), params.isDecrease());

            List<Long> order = listCollectionsResult.getOrder();

            Map<Long, CollectionData> resultMap = listCollectionsResult.getCollectionDataMap();

            for (int i = 0; i < order.size(); i++) {
                long tmp = order.get(i);
                result.add(Collection.fromCollectionData(tmp, resultMap.get(tmp)));
            }
            return result;
        } else {
            listCollectionsResult = collectionStorage.listCollections();
            return listCollectionsResult.getCollectionDataMap()
                    .entrySet()
                    .stream()
                    .map(entry -> Collection.fromCollectionData(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }

    }
}
