import React, {useEffect} from "react";
import {observer} from "mobx-react-lite"
import {Button, Spinner, Table} from "react-bootstrap";

import CollectionDialog from "./CollectionDialog";
import ColumnHeader from "./ColumnHeader";

function HeaderWithSort(props) {
    const {collectionStore, column, displayName} = props;
    return (
        <th>
            <ColumnHeader
                displayName={displayName}
                column={column}
                sortBy={collectionStore.sortingColumn}
                sortDirection={collectionStore.sortingOrder}
                onSortAscending={() => collectionStore.setSort(column, 'asc')}
                onSortDescending={() => collectionStore.setSort(column, 'desc')}
                onClearSort={() => collectionStore.resetSort()}
            />
        </th>
    );
}

const CollectionsTable = observer(({collectionStore}) => {
    const collections = collectionStore.collections;

    if (collections.length === 0) {
        return (<p>You do not have any collection yet</p>);
    }
    return (
        <Table>
            <thead>
            <tr>
                <HeaderWithSort column='collectionName' displayName='Name' collectionStore={collectionStore}/>
                <HeaderWithSort column='ownerEmail' displayName='Created by' collectionStore={collectionStore}/>
                <HeaderWithSort column='createdAtUtc' displayName='Creation date' collectionStore={collectionStore}/>
                <HeaderWithSort column='updatedAtUtc' displayName='Update date' collectionStore={collectionStore}/>
                <th/>
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
                            </td>
                            <td>
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
                                            <div>
                                                <Button
                                                    variant="warning"
                                                    onClick={() => collectionStore.archiveCollectionAction(collectionId)}
                                                >
                                                    Archive
                                                </Button>
                                                <Button
                                                    variant="primary"
                                                    onClick={() => collectionStore.startEditingCollection(collectionId)}
                                                >
                                                    Edit
                                                </Button>
                                            </div>
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
        collectionStore.stopEditingCollection();
    }

    function handleCreateCompleted() {
        collectionStore.stopEditingCollection();
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
                    collection={collectionStore.editedCollection}
                    show={collectionStore.editingCollection}
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