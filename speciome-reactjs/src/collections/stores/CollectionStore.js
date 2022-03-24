import {makeAutoObservable} from "mobx"

import {archiveCollection, deleteCollection, listCollections, unArchiveCollection} from "../api/CollectionsAPI";

export default class CollectionStore {
    editingCollection = false;
    editedCollectionId = null;
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
        this.editingCollection = true;
        this.editedCollectionId = null;
    }

    stopEditingCollection() {
        this.editingCollection = false;
        this.editedCollectionId = null;
    }

    startEditingCollection(collectionId) {
        this.editingCollection = true;
        this.editedCollectionId = collectionId;
    }

    reloadCollections() {
        this.setFetching(true);
        listCollections().then(listResponse => {
            this.setCollections(listResponse.collections);
            this.setFetching(false);
        })
    }

    get editedCollection() {
        return this.collections.find(({collectionId}) => collectionId === this.editedCollectionId);
    }

    setCollections(collections) {
        this.collections = collections;
    }

    setFetching(fetching) {
        this.fetching = fetching;
    }
}