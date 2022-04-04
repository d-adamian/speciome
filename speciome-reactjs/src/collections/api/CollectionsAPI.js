import axios from "axios";

const BASE_URL = ''

function addCollection(name) {
    const url = `${BASE_URL}/collection`;

    const payload = {
        collectionName: name
    }

    return axios.post(url, payload)
        .then(response => response.data)
        .then(({collectionId}) => collectionId);
}

function archiveCollection(collectionId) {
    const url = `${BASE_URL}/collection/${collectionId}/archive`;

    return axios.put(url)
        .then(response => response.data);
}

function deleteCollection(collectionId) {
    const url = `${BASE_URL}/collection/${collectionId}`

    return axios.delete(url);
}

function listCollections(sortBy, orderBy) {
    const url = `${BASE_URL}/collections`

    if (sortBy) {
        const params = { sortBy, orderBy };
        return axios.get(url, { params: params })
            .then(response => response.data);
    } else {
        return axios.get(url)
            .then(response => response.data);
    }
}

function unArchiveCollection(collectionId) {
    const url = `${BASE_URL}/collection/${collectionId}/unarchive`;

    return axios.put(url)
        .then(response => response.data);
}

function updateCollection(collectionId, collection) {
    const url = `${BASE_URL}/collection/${collectionId}`
    const payload = collection;

    return axios.put(url, payload)
        .then(response => response.data);
}

export {
    BASE_URL, addCollection, archiveCollection, deleteCollection, listCollections, unArchiveCollection,
    updateCollection
};