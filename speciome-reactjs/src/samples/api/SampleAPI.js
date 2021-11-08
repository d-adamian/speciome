import axios from 'axios';

const BASE_URL = ''

function addSample() {
    const url = `${BASE_URL}/sample`;

    // noinspection JSCheckFunctionSignatures
    return axios.post(url)
        .then(response => response.data)
        .then(({sampleId}) => sampleId);
}

function deleteSample(sampleId) {
    const url = `${BASE_URL}/sample/${sampleId}`;

    return axios.delete(url);
}

function findSamples() {
    const url = `${BASE_URL}/samples`

    return axios.get(url).then(response => response.data);
}

function listAttributes() {
    const url = `${BASE_URL}/attributes`

    return axios.get(url).then(response => response.data);
}

function updateSample({sampleId, attributes}) {
    const url = `${BASE_URL}/sample/${sampleId}`;

    const payload = {attributes: attributes};
    return axios.put(url, payload);
}

export {BASE_URL, addSample, deleteSample, findSamples, listAttributes, updateSample};