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

function findSamples(archivalStatus) {
    const url = `${BASE_URL}/samples`

    return axios.get(url, {
        params: {
            archivalStatus: archivalStatus
        }
    }).then(response => response.data);
}

function listAttributes() {
    const url = `${BASE_URL}/attributes`

    return axios.get(url).then(response => response.data);
}

function archiveSample(sampleId) {
    const url = `${BASE_URL}/sample/${sampleId}/archive`;

    return axios.put(url).then(response => response.data);
}

function unArchiveSample(sampleId) {
    const url = `${BASE_URL}/sample/${sampleId}/unarchive`;

    return axios.put(url).then(response => response.data);
}

function updateSample({sampleId, attributes}) {
    const url = `${BASE_URL}/sample/${sampleId}`;

    const payload = {attributes: attributes};
    return axios.put(url, payload);
}

function importCSV(csvFile) {
    const url = `${BASE_URL}/samples/upload/csv`;

    const formData = new FormData();
    formData.append('file', csvFile, csvFile.name);

    return axios.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

export {
    BASE_URL, addSample, archiveSample, deleteSample, findSamples, importCSV, listAttributes, unArchiveSample,
    updateSample
};