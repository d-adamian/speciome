<template>
    <div>
        <div>
            <div v-if="isTableEmpty">
                No samples found
            </div>
            <div v-else>
                <!-- eslint-disable -->
                <b-table :items="samples" :fields="tableFields">
                    <template v-for="field in editableFields" v-slot:[`cell(${field.key})`]="{ value, item, field }">
                        <b-input
                                v-if="selectedRow && selectedRow.sampleId === item.sampleId"
                                v-model="selectedRow[field.key]" :type="'text'">
                        </b-input>
                        <template v-else>{{ value }}</template>
                    </template>
                    <template v-slot:cell(actions)="{ item }">
                        <div v-if="selectedRow && selectedRow.sampleId === item.sampleId">
                            <b-button-group>
                                <b-btn variant="success" @click="saveEdit">
                                    Save
                                </b-btn>
                                <b-btn variant="danger" @click="resetEdit">
                                    Cancel
                                </b-btn>
                            </b-button-group>
                        </div>
                        <div v-else>
                            <b-btn variant="primary" @click="editRow(item)">
                                Edit
                            </b-btn>
                            <b-btn variant="danger" @click="deleteRow(item)">
                                Delete
                            </b-btn>
                        </div>
                    </template>
                </b-table>
            </div>

            <button type="submit" class="btn btn-primary" @click.prevent="addSample">
                Add Sample
            </button>
        </div>
    </div>
</template>
<script>
const attributesMap = new Map()
    .set('collectorName', 'Collector name')
    .set('placeOfCollection', 'Place of collection')
    .set('dateOfCollection', 'Date of collection')
    .set('sampleTaxonomy', 'Sample taxonomy');

export default {
    name: "SamplesTable",
    data: () => ({
        attributesMap: attributesMap,
        selectedRow: null,
        tableFields: [],
        samples: []
    }),
    computed: {
        isTableEmpty: function() {
            return this.samples.length == 0;
        },
        editableFields() {
            return this.tableFields.filter((field) => field.editable);
        }
    },
    methods: {
        loadTableFields() {
            // TODO: load from server
            let tableFields = [{key: 'sampleId', label: 'Identifier'}]
            this.attributesMap.forEach((name, key) => {
                const field = {
                    key: key,
                    label: name,
                    editable: true
                };
                tableFields.push(field);
            });
            tableFields.push({key: 'actions'});
            this.tableFields = tableFields;
        },
        loadSamples() {
            fetch("/samples")
                .then(response => {
                    if (response.status == 200) {
                        return response.json();
                    } else if (response.status == 401) {
                        // TODO: check authorization in one place
                        window.location.href = "/#sign-up";
                    } else {
                        alert("Error while fetching samples");
                    }
                 })
                .then(res => {
                    // TODO: handle incorrect output format
                    const samples = res.samples.map(smpl => {
                        let tableRow = {sampleId: smpl.sampleId};
                        smpl.attributes.forEach(attrValuePair => {
                            tableRow[attrValuePair['attribute']] = attrValuePair['value'];
                        });
                        return tableRow;
                    });
                    this.samples = samples;
                })
                .catch(() => alert("Error while fetching samples"));
        },
        addSample() {
            const newSampleAttributes = Array.from(this.attributesMap.keys()).map(attr => {
                return {
                    attribute: attr,
                    value: ""
                }
            });
            const newSample = {
                sampleId: "",
                modified: true,
                attributes: newSampleAttributes
            };

            fetch("/sample", {
                method: "post",
                headers: {
                  'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    attributes: newSample.attributes
                })
            })
            .then(response => {
                if (response.status == 201) {
                    return response.json();
                } else if (response.status == 401) {
                    // TODO: check authorization in one place
                    window.location.href = "/#sign-up";
                } else {
                    alert("Error while adding sample");
                }
             })
            .then(res => {
                console.log(res);
                const sampleWithId = newSample;
                sampleWithId.sampleId = res.sampleId;
                this.samples.push(sampleWithId);
            })
            .catch(() => alert("Error while adding sample"));
        },
        editRow(row) {
            let doEdit = true;
            if (
                this.selectedRow && !confirm("You have unsaved changes, are you sure you want to continue?")
            ) {
              doEdit = false;
            }

            if (doEdit) {
              this.selectedRow = { ...row };
            }
          },
        deleteRow(row) {
            const sampleId = row.sampleId;
            fetch(`/sample/${sampleId}`, {
                method: "delete"
            })
            .then(response => {
                if (response.status == 204) {
                    this.samples = this.samples.filter(s => s.sampleId != row.sampleId);
                } else if (response.status == 401) {
                    // TODO: check authorization in one place
                    window.location.href = "/#sign-up";
                } else {
                    alert("Error while deleting sample");
                }
             })
            .catch(() => alert("Error while deleting sample"));
        },
        saveEdit() {
            const sampleId = this.selectedRow.sampleId;
            const sample = this.samples.find((s) => s.sampleId === this.selectedRow.sampleId);

            const selectedRow = this.selectedRow;
            const attributes = [];
            this.attributesMap.forEach((unused, key) => {
                attributes.push({
                    attribute: key,
                    value: selectedRow[key]
                });
            });

            fetch(`/sample/${sampleId}`, {
                method: "put",
                headers: {
                  'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    attributes: attributes
                })
            })
            .then(response => {
                if (response.status == 204) {
                    return;
                } else if (response.status == 401) {
                    // TODO: check authorization in one place
                    window.location.href = "/#sign-up";
                } else {
                    alert("Error while updating sample");
                }
             }).then(() => {
                    const updatedSample = {sampleId: sampleId};
                    attributes.forEach(attrValuePair => {
                        updatedSample[attrValuePair['attribute']] = attrValuePair['value'];
                    });
                    Object.assign(sample, updatedSample);
                    this.resetEdit();
             })
            .catch(() => alert("Error while updating sample"));
        },
        resetEdit() {
            this.selectedRow = null;
        },
    },
    created() {
        this.loadTableFields();
        this.loadSamples();
    }
}
</script>

<style>
</style>