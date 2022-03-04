import {makeAutoObservable} from "mobx"

import {
    addSample,
    archiveSample,
    deleteSample,
    findSamples,
    listAttributes,
    unArchiveSample,
    updateSample
} from "../api/SampleAPI";

export default class SamplesStore {
    fetching = false
    importing = false
    selectedId = null
    columnNames = []
    archivalStatus = 'ALL'
    samples = []

    constructor() {
        makeAutoObservable(this);
    }

    reloadSamples() {
        this.fetching = true;
        listAttributes().then(attributes => {
            findSamples(this.archivalStatus).then(response => {
                this.updateTable(attributes, response.samples);
            });
        });
    }

    updateTable(attributes, samples) {
        this.fetching = false;
        this.selectedId = null;
        this.columnNames = attributes;
        this.samples = samples;
    }

    onDeleteSample(sampleId) {
        deleteSample(sampleId).then(() => this.doDeleteSample(sampleId));
    }

    doDeleteSample(sampleIdToDelete) {
        this.samples = this.samples.filter(({sampleId}) => sampleId !== sampleIdToDelete);
    }

    onAddSample() {
        addSample().then(sampleId => {
            this.doAddSample(sampleId)
        })
    }

    doAddSample(sampleId) {
        const attributes = this.columnNames.map((columnName) => {
            return {attribute: columnName, value: ''};
        });
        const sample = {
            sampleId: sampleId,
            attributes: attributes
        }

        this.samples = [...this.samples, sample];
    }

    replaceSampleInState(sampleId, changedSample) {
        this.samples = this.samples.map((sample) => {
            if (sample.sampleId === changedSample.sampleId) {
                return changedSample;
            } else {
                return sample;
            }
        });
    }

    saveNewSample(changedSample) {
        updateSample(changedSample).then(() => {
            this.replaceSampleInState(changedSample.sampleId, changedSample);
            this.finishSampleEdit();
        })
    }

    startSampleEdit(sampleId) {
        this.selectedId = sampleId;
    }

    finishSampleEdit() {
        this.selectedId = null;
    }

    startImporting() {
        this.importing = true;
    }

    importCompleted() {
        this.importing = false;
        this.reloadTable();
    }

    importCancelled() {
        this.importing = false;
    }

    archiveSample(sampleId) {
        archiveSample(sampleId).then((sample) => {
            this.replaceSampleInState(sampleId, sample);
        });
    }

    restoreSample(sampleId) {
        unArchiveSample(sampleId).then((sample) => {
            this.replaceSampleInState(sampleId, sample);
        });
    }

    changeArchivalStatus(archivalStatus) {
        this.archivalStatus = archivalStatus;
        this.reloadSamples();
    }

}