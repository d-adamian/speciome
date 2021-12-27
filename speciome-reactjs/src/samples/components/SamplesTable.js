import React, {useEffect, useState} from 'react';

import Button from 'react-bootstrap/Button'
import Col from 'react-bootstrap/Col'
import Container from 'react-bootstrap/Container'
import Form from 'react-bootstrap/Form'
import Row from 'react-bootstrap/Row'
import Spinner from 'react-bootstrap/Spinner'
import Table from 'react-bootstrap/Table'

import {
    addSample,
    archiveSample,
    deleteSample,
    findSamples,
    listAttributes,
    unArchiveSample,
    updateSample
} from "../api/SampleAPI";
import ImportDialog from "./ImportDialog";
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
    const {columnNames, sample, editDisabled, onDelete, onEdit, onArchive, onRestore} = props;
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
                {
                    sample.archived ?
                    (
                        <div>
                            <Button variant="primary" onClick={onRestore}>Restore</Button>
                            <Button variant="danger" onClick={onDelete}>Delete</Button>
                        </div>
                    ) :
                    (
                        <div>
                            <Button variant="primary" disabled={editDisabled} onClick={onEdit}>Edit</Button>
                            <Button variant="danger" onClick={onArchive}>Archive</Button>
                        </div>
                    )
                }
            </td>
        </tr>
    );
}

function SamplesTable() {
    const [fetching, setFetching] = useState(true);
    const [importing, setImporting] = useState(false);
    const [selectedId, setSelectedId] = useState(null);
    const [columnNames, setColumnNames] = useState([]);
    const [archivalStatus, setArchivalStatus] = useState('ALL');
    const [samples, setSamples] = useState([]);

    function reloadTable() {
        listAttributes().then(attributes => {
            findSamples(archivalStatus).then(response => {
                setFetching(false);
                setSelectedId(null);
                setColumnNames(attributes);
                setSamples(response.samples);
            });
        });
    }

    useEffect(() => {
        reloadTable();

        return () => {
            setFetching(false);
            setSelectedId(null);
            setColumnNames([]);
            setSamples([]);
        };
    }, [archivalStatus]);

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

    function replaceSampleInState(sampleId, changedSample) {
        const updatedSamples = samples.map((sample) => {
            if (sample.sampleId === changedSample.sampleId) {
                return changedSample;
            } else {
                return sample;
            }
        });

        setSamples(updatedSamples);
    }

    function handleSave(changedSample) {
        updateSample(changedSample).then(() => {
            replaceSampleInState(changedSample.sampleId, changedSample);
            setSelectedId(null);
        })
    }

    function handleEdit(sampleId) {
        setSelectedId(sampleId);
    }

    function handleCancelEdit() {
        setSelectedId(null);
    }

    function handleImport() {
        setImporting(true);
    }

    function handleImportCompleted() {
        setImporting(false);
        reloadTable();
    }

    function handleImportCancelled() {
        setImporting(false);
    }

    function handleArchive(sampleId) {
        archiveSample(sampleId).then((sample) => {
            replaceSampleInState(sampleId, sample);
        });
    }

    function handleRestore(sampleId) {
        unArchiveSample(sampleId).then((sample) => {
            replaceSampleInState(sampleId, sample);
        });
    }

    function handleArchivalStatusChange(event) {
        setArchivalStatus(event.target.value);
    }

    if (fetching) {
        return (<Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
        </Spinner>);
    } else {
        return (
            <Container fluid>
                <ImportDialog show={importing} onCancel={handleImportCancelled} onComplete={handleImportCompleted}/>
                <Row>
                    <Col md="auto">
                        <Form.Group controlId="showSamplesOption">
                            <Form.Label>Show samples</Form.Label>
                            <Form.Select
                                value={archivalStatus}
                                onChange={handleArchivalStatusChange}
                            >
                                <option value="ALL">All samples</option>
                                <option value="ARCHIVED">Archived only</option>
                                <option value="UNARCHIVED">UnArchived only</option>
                            </Form.Select>
                        </Form.Group>
                        <Button variant="primary" onClick={handleAddSample}>Add sample</Button>
                    </Col>
                    <Col>
                        <div className="float-end">
                            <Button variant="outline-primary" href="http://localhost:8081/samples/download">
                                Export to CSV
                            </Button>
                            <Button variant="outline-primary" onClick={handleImport}>Import</Button>
                        </div>
                    </Col>
                </Row>
                <Row>
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
                                    onArchive={() => handleArchive(sample.sampleId)}
                                        onDelete={() => handleDelete(sample.sampleId)}
                                        onEdit={() => handleEdit(sample.sampleId)}
                                    onRestore={() => handleRestore(sample.sampleId)}
                                    />
                                );
                            }
                        })
                        }
                        </tbody>
                    </Table>
                </Row>
            </Container>
        );
    }
}

export default SamplesTable;