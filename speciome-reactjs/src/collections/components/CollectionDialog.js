import Button from "react-bootstrap/Button";
import {Form, Modal} from "react-bootstrap";
import {useState} from "react";
import {addCollection} from "../api/CollectionsAPI";

function EmptyNameWarning(props) {
    if (props.isNameEmpty) {
        return <Form.Text className="text-muted">Collection name is empty</Form.Text>;
    } else {
        return null;
    }
}

function CollectionDialog(props) {
    const {onCancel, onComplete, show} = props
    const [name, setName] = useState('');
    const isNameEmpty = name === undefined || name.length === 0;

    function handleSave() {
        addCollection(name).then((collectionId) => {
            setName('');
            onComplete(collectionId);
        })
    }

    function handleCancel() {
        setName('');
        onCancel();
    }

    return (
        <Modal show={show} onHide={() => handleCancel()}>
            <Modal.Header closeButton>
                Create new collection
            </Modal.Header>
            <Modal.Body>
                <Form.Group controlId="collectionName">
                    <Form.Label>Name</Form.Label>
                    <Form.Control
                        placeholder="My new collection"
                        value={name}
                        onChange={event => setName(event.target.value)}
                    />
                    <EmptyNameWarning isNameEmpty={isNameEmpty}/>
                </Form.Group>
            </Modal.Body>
            <Modal.Footer>
                <Button
                    variant='primary'
                    disabled={isNameEmpty}
                    onClick={() => handleSave()}
                >
                    Save
                </Button>
                <Button
                    variant='outline-primary'
                    onClick={() => handleCancel()}
                >
                    Cancel
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

CollectionDialog.defaultProps = {
    show: true
};

export default CollectionDialog;