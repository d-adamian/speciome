import {makeAutoObservable} from "mobx"

import {deleteCollection, listCollections} from "../api/CollectionsAPI";

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