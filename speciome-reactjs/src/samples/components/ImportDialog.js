import React, {useState} from 'react';

import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';

import {importCSV} from "../api/SampleAPI";

function ImportDialog(props) {
    const {onCancel, onComplete, maxSizeKb, show} = props;

    const [selectedFile, setSelectedFile] = useState(null);
    const [fileTooLarge, setFileTooLarge] = useState(false);

    const buttonDisabled = (fileTooLarge === true) || (selectedFile === null);

    function handleFileInput(event) {
        const file = event.target.files[0];
        setSelectedFile(file);

        const fileSizeKb = file.size / 1024;
        if (fileSizeKb > maxSizeKb) {
            setFileTooLarge(true);
        } else {
            setFileTooLarge(false);
        }
    }

    function handleImport(event) {
        importCSV(selectedFile).then(response => {
            onComplete();
        });
    }

    return (
        <Modal show={show} onHide={onCancel}>
            <Modal.Header closeButton>
                <Modal.Title>Import table</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <ul>
                    <li>You can import only CSV files</li>
                    <li>Max file size is 10 Mb</li>
                </ul>
                <Form.Group controlId="fileChooser">
                    <Form.Label>File</Form.Label>
                    <Form.Control type="file" onChange={handleFileInput}/>
                </Form.Group>
            </Modal.Body>
            <Modal.Footer>
                <Form.Group controlId="importButton">
                    <Button variant="primary" disabled={buttonDisabled} onClick={handleImport}>Import</Button>
                    {fileTooLarge && <Form.Text className="text-muted">File is too large</Form.Text>}
                </Form.Group>
            </Modal.Footer>
        </Modal>
    )
};

ImportDialog.defaultProps = {
    maxSizeKb: 10 * 1024,
    show: true
}

export default ImportDialog