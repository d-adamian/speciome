import Button from "react-bootstrap/Button";
import {Form, Modal} from "react-bootstrap";
import {useState} from "react";
import {addCollection, updateCollection} from "../api/CollectionsAPI";

function EmptyNameWarning(props) {
    if (props.isNameEmpty) {
        return <Form.Text className="text-muted">Collection name is empty</Form.Text>;
    } else {
        return null;
    }
}

function CollectionDialog(props) {
    const {collection, onCancel, onComplete, show} = props;
    const isNewCollection = collection === null | collection === undefined;
    const initialName = isNewCollection ? '' : collection.collectionName;

    const [name, setName] = useState('');
    const isNameEmpty = name === undefined || name.length === 0;

    function setNameOnShow() {
        setName(initialName);
    }

    function handleSave() {
        if (isNewCollection) {
            addCollection(name).then((collectionId) => {
                onComplete(collectionId);
            });
        } else {
            const {collectionId} = collection;
            const modifiedCollection = {
                collectionName: name
            }
            updateCollection(collectionId, modifiedCollection).then(() => {
                onComplete(collectionId);
            });
        }
    }

    function handleCancel() {
        setName(initialName);
        onCancel();
    }

    return (
        <Modal
            show={show}
            onHide={() => handleCancel()}
            onShow={() => setNameOnShow()}
        >
            <Modal.Header closeButton>
                {isNewCollection ? "Create new collection" : "Edit collection"}
            </Modal.Header>
            <Modal.Body>
                <Form.Group controlId="collectionName">
                    <Form.Label>Name</Form.Label>
                    <Form.Control
                        placeholder={isNewCollection ? "My new collection" : ''}
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
                    {isNewCollection? "Save" : "Update"}
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