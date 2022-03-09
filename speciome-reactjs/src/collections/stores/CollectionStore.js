import {makeAutoObservable} from "mobx"

import {archiveCollection, deleteCollection, listCollections, unArchiveCollection} from "../api/CollectionsAPI";

export default class CollectionStore {
    creatingCollection = false
    fetching = true
    collections = []

    constructor() {
        makeAutoObservable(this);
    }

    addCollection(collection) {
        this.collections.push(collection);
    }

    removeCollection(collectionIdToRemove) {
        deleteCollection(collectionIdToRemove).then(() => {
            const updatedCollections = this.collections.filter(
                ({collectionId}) => collectionId !== collectionIdToRemove
            );
            this.setCollections(updatedCollections)
        })
    }

    archiveCollectionAction(collectionId) {
        archiveCollection(collectionId).then((collection) => {
            this.replaceCollectionInState(collectionId, collection);
        })
    }

    restoreCollectionAction(collectionId) {
        unArchiveCollection(collectionId).then((collection) => {
            this.replaceCollectionInState(collectionId, collection);
        })
    }

    replaceCollectionInState(collectionId, changedCollection) {
        const updatedCollections = this.collections.map((collection) => {
            if (collection.collectionId === collectionId) {
                return changedCollection;
            } else {
                return collection;
            }
        });
        this.setCollections(updatedCollections);
    }

    startCreatingCollection() {
        this.creatingCollection = true;
    }

    stopCreatingCollection() {
        this.creatingCollection = false;
    }

    reloadCollections() {
        this.setFetching(true);
        listCollections().then(listResponse => {
            this.setCollections(listResponse.collections);
            this.setFetching(false);
        })
    }

    setCollections(collections) {
        this.collections = collections;
    }

    setFetching(fetching) {
        this.fetching = fetching;
    }
}