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

export {BASE_URL, addCollection};