import React, {useEffect} from 'react';
import { observer } from "mobx-react-lite"

import Button from 'react-bootstrap/Button'
import Col from 'react-bootstrap/Col'
import Container from 'react-bootstrap/Container'
import Form from 'react-bootstrap/Form'
import Row from 'react-bootstrap/Row'
import Spinner from 'react-bootstrap/Spinner'
import Table from 'react-bootstrap/Table'

import ImportDialog from "./ImportDialog"
import EditableRow from "./EditableRow"
import SortingIcons from '../../utils/components/SortingIcons';

function TableHeader(props) {
    const { columnNames, samplesStore } = props;
    return (
        <thead>
        <tr>
            <th/>
            {
                columnNames.map((name, index) => (
                    <th>
                        <SortingIcons
                            displayName={name}
                            column={name}
                            sortBy={samplesStore.sortingColumn}
                            sortDirection={samplesStore.sortingOrder}
                            onSortAscending={() => samplesStore.setSort(name, 'asc')}
                            onSortDescending={() => samplesStore.setSort(name, 'desc')}
                            onClearSort={() => samplesStore.resetSort()}
                        />
                    </th>
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
                            <Button variant="warning" onClick={onArchive}>Archive</Button>
                        </div>
                    )
                }
            </td>
        </tr>
    );
}

const SamplesTable = observer(({ samplesStore }) => {
    useEffect(() => {
        samplesStore.reloadSamples();
    }, [samplesStore]);

    if (samplesStore.fetching) {
        return (<Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
        </Spinner>);
    } else {
        return (
            <Container fluid>
                <ImportDialog
                    show={samplesStore.importing}
                    onCancel={() => samplesStore.importCancelled()}
                    onComplete={() => samplesStore.importCompleted()}
                />
                <Row>
                    <Col md="auto">
                        <Form.Group controlId="showSamplesOption">
                            <Form.Label>Show samples</Form.Label>
                            <Form.Select
                                value={samplesStore.archivalStatus}
                                onChange={(event) => samplesStore.changeArchivalStatus(event.target.value)}
                            >
                                <option value="ALL">All samples</option>
                                <option value="ARCHIVED">Archived only</option>
                                <option value="UNARCHIVED">UnArchived only</option>
                            </Form.Select>
                        </Form.Group>
                        <Button
                            variant="primary"
                            onClick={() => samplesStore.onAddSample()}
                        >
                            Add sample
                        </Button>
                    </Col>
                    <Col>
                        <div className="float-end">
                            <Button variant="outline-primary" href="http://localhost:8081/samples/download">
                                Export to CSV
                            </Button>
                            <Button
                                variant="outline-primary"
                                onClick={samplesStore.startImporting}
                            >
                                Import
                            </Button>
                        </div>
                    </Col>
                </Row>
                <Row>
                    <Table>
                        <TableHeader columnNames={samplesStore.columnNames} samplesStore={samplesStore}/>
                        <tbody>
                        {samplesStore.samples &&
                        samplesStore.samples.map((sample, index) => {
                            if (sample.sampleId === samplesStore.selectedId) {
                                return (
                                    <EditableRow
                                        key={index}
                                        columnNames={samplesStore.columnNames}
                                        sample={sample}
                                        onCancel={samplesStore.finishSampleEdit}
                                        onSave={(updatedSample) => samplesStore.saveNewSample(updatedSample)}
                                    />
                                )
                            } else {
                                return (
                                    <TableRow
                                        key={index}
                                        sample={sample}
                                        columnNames={samplesStore.columnNames}
                                        editDisabled={samplesStore.selectedId !== null}
                                        onArchive={() => samplesStore.archiveSample(sample.sampleId)}
                                        onDelete={() => samplesStore.onDeleteSample(sample.sampleId)}
                                        onEdit={() => samplesStore.startSampleEdit(sample.sampleId)}
                                        onRestore={() => samplesStore.restoreSample(sample.sampleId)}
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
})

export default SamplesTable;