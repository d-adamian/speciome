import React, {useEffect} from "react";
import {observer} from "mobx-react-lite"
import {Button, Spinner, Table} from "react-bootstrap";

import CollectionDialog from "./CollectionDialog";

const CollectionsTable = observer(({collectionStore}) => {
    const collections = collectionStore.collections;

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
                <th>
                    Created by
                </th>
                <th>
                    Created/Updated
                </th>
                <th>

                </th>
            </tr>
            </thead>
            <tbody>
            {
                collections.map((collection, index) => {
                    const {
                        collectionId,
                        collectionName,
                        ownerEmail,
                        archived,
                        createdAt,
                        updatedAt
                    } = collection;

                    return (
                        <tr key={index}>
                            <td>
                                {collectionName}
                            </td>
                            <td>
                                {ownerEmail}
                            </td>
                            <td>
                                {new Date(createdAt).toLocaleString('en-GB')}
                                /
                                {new Date(updatedAt).toLocaleString('en-GB')}
                            </td>
                            <td>
                                {
                                    archived ?
                                        (
                                            <div>
                                                <Button
                                                    variant="primary"
                                                    onClick={() => collectionStore.restoreCollectionAction(collectionId)}
                                                >
                                                    Restore
                                                </Button>
                                                <Button
                                                    variant="danger"
                                                    onClick={() => collectionStore.removeCollection(collectionId)}
                                                >
                                                    Remove
                                                </Button>
                                            </div>
                                        ) :
                                        (
                                            <Button
                                                variant="warning"
                                                onClick={() => collectionStore.archiveCollectionAction(collectionId)}
                                            >
                                                Archive
                                            </Button>

                                        )
                                }
                            </td>
                        </tr>
                    )
                })
            }
            </tbody>
        </Table>
    )
})

const CollectionsList = observer(({collectionStore}) => {
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
            <div>
                <CollectionDialog
                    show={collectionStore.creatingCollection}
                    onCancel={handleCreateCancelled}
                    onComplete={handleCreateCompleted}
                />
                <CollectionsTable collectionStore={collectionStore}/>
                <Button onClick={() => collectionStore.startCreatingCollection()}>
                    Create Collection
                </Button>
            </div>
        );
    }
})

export default CollectionsList;