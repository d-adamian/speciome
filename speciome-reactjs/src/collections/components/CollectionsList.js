import Button from "react-bootstrap/Button";
import {useState} from "react";
import CollectionDialog from "./CollectionDialog";
import {Alert} from "react-bootstrap";

function CreateSuccessAlert(props) {
    const {collectionId, onClose} = props;
    if (collectionId !== null) {
        return <Alert variant="success" dismissible onClose={onClose}>Created collection {collectionId}</Alert>
    } else {
        return null;
    }
}

function CollectionsList() {
    const [creating, setCreating] = useState(false);
    const [collectionId, setCollectionId] = useState(null);

    function handleCreateCancelled() {
        setCreating(false);
    }

    function handleCreateCompleted(collectionId) {
        setCreating(false);
        setCollectionId(collectionId);
    }

    return (
        <div className="speciome-collections-list">
            <CollectionDialog
                show={creating}
                onCancel={handleCreateCancelled}
                onComplete={handleCreateCompleted}
            />
            <CreateSuccessAlert
                collectionId={collectionId}
                onClose={() => setCollectionId(null)}
            />
            <p>You do not have any collection yet</p>
            <Button onClick={() => setCreating(true)}>
                Create Collection
            </Button>
        </div>
    );
}

export default CollectionsList;