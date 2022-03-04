import React, {useEffect} from "react";
import {observer} from "mobx-react-lite"
import {Button, Spinner, Table} from "react-bootstrap";

import CollectionDialog from "./CollectionDialog";


function CollectionsTable(props) {
    const {collections} = props;

    if (collections.length === 0) {
        return (<p>You do not have any collection yet</p>);
    }
    return (
        <Table>
            <thead>
            <tr>
                <th>
                    Name
                </th>
            </tr>
            </thead>
            <tbody>
            {
                collections.map(({collectionName}, index) => {
                    return (
                        <tr key={index}>
                            <td>
                                {collectionName}
                            </td>
                        </tr>
                    )
                })
            }
            </tbody>
        </Table>
    )
}

const CollectionsList = observer(({ collectionStore }) => {
    useEffect(() => {
        collectionStore.reloadCollections();
    }, [collectionStore]);

    function handleCreateCancelled() {
        collectionStore.stopCreatingCollection();
    }

    function handleCreateCompleted() {
        collectionStore.stopCreatingCollection();
        collectionStore.reloadCollections();
    }

    if (collectionStore.fetching) {
        return (<Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
        </Spinner>);
    } else {
        return (
            <div className="speciome-collections-list">
                <CollectionDialog
                    show={collectionStore.creatingCollection}
                    onCancel={handleCreateCancelled}
                    onComplete={handleCreateCompleted}
                />
                <CollectionsTable collections={collectionStore.collections}/>
                <Button onClick={() => collectionStore.startCreatingCollection()}>
                    Create Collection
                </Button>
            </div>
        );
    }
})

export default CollectionsList;