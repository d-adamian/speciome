import React, {useEffect, useState} from 'react';

import Button from 'react-bootstrap/Button'
import Spinner from 'react-bootstrap/Spinner'
import Table from 'react-bootstrap/Table'

import {addSample, deleteSample, findSamples, listAttributes, updateSample} from "../api/SampleAPI";
import EditableRow from "./EditableRow";

function TableHeader(props) {
    return (
        <thead>
        <tr>
            <th/>
            {
                props.columnNames.map((name, index) => (
                    <th key={index}>{name}</th>
                ))
            }
            <th/>
        </tr>
        </thead>
    );
}

function TableRow(props) {
    const {columnNames, sample, editDisabled, onDelete, onEdit} = props;
    const kvPairs = sample.attributes.map(({attribute, value}) => [attribute, value]);
    const attributeMap = new Map(kvPairs);

    return (
        <tr>
            <td>{sample.sampleId}</td>
            {
                columnNames.map((name, index) => {
                    const value = attributeMap.get(name);
                    return (<td key={index}>{value}</td>);
                })
            }
            <td>
                <Button variant="primary" disabled={editDisabled} onClick={onEdit}>Edit</Button>
                <Button variant="danger" onClick={onDelete}>Delete</Button>
            </td>
        </tr>
    );
}

function SamplesTable() {
    const [fetching, setFetching] = useState(true);
    const [selectedId, setSelectedId] = useState(null);
    const [columnNames, setColumnNames] = useState([]);
    const [samples, setSamples] = useState([]);

    useEffect(() => {
        function fetchInitialData() {
            listAttributes().then(attributes => {
                findSamples().then(response => {
                    setFetching(false);
                    setSelectedId(null);
                    setColumnNames(attributes);
                    setSamples(response.samples);
                });
            })
        }

        fetchInitialData();
        return () => {
            setFetching(false);
            setSelectedId(null);
            setColumnNames([]);
            setSamples([]);
        };
    }, []);

    function handleDelete(sampleIdToDelete) {
        deleteSample(sampleIdToDelete).then(() => {
            const updatedSamples = samples.filter(({sampleId}) => sampleId !== sampleIdToDelete);
            setSamples(updatedSamples);
        })
    }

    function handleAddSample() {
        addSample().then(sampleId => {
            const attributes = columnNames.map((columnName) => {
                return {attribute: columnName, value: ''};
            });
            const sample = {
                sampleId: sampleId,
                attributes: attributes
            }

            setSamples([...samples, sample]);
        })
    }

    function handleSave(changedSample) {
        updateSample(changedSample).then(() => {
            const updatedSamples = samples.map((sample) => {
                if (sample.sampleId === changedSample.sampleId) {
                    return changedSample;
                } else {
                    return sample;
                }
            });

            setSamples(updatedSamples);
            setSelectedId(null);
        })
    }

    function handleEdit(sampleId) {
        setSelectedId(sampleId);
    }

    function handleCancelEdit() {
        setSelectedId(null);
    }

    if (fetching) {
        return (<Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
        </Spinner>);
    } else {
        return (
            <div>
                <Table>
                    <TableHeader columnNames={columnNames}/>
                    <tbody>
                    {samples &&
                    samples.map((sample, index) => {
                        if (sample.sampleId === selectedId) {
                            return (
                                <EditableRow
                                    key={index}
                                    columnNames={columnNames}
                                    sample={sample}
                                    onCancel={handleCancelEdit}
                                    onSave={(updatedSample) => handleSave(updatedSample)}
                                />
                            )
                        } else {
                            return (
                                <TableRow
                                    key={index}
                                    sample={sample}
                                    columnNames={columnNames}
                                    editDisabled={selectedId !== null}
                                    onDelete={() => handleDelete(sample.sampleId)}
                                    onEdit={() => handleEdit(sample.sampleId)}
                                />
                            );
                        }
                    })
                    }
                    </tbody>
                </Table>
                <Button variant="primary" onClick={handleAddSample}>Add sample</Button>
            </div>
        );
    }
}

export default SamplesTable;