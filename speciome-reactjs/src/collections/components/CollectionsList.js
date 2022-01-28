import React, {useEffect, useState} from "react";
import CollectionDialog from "./CollectionDialog";

import {Button, Spinner, Table} from "react-bootstrap";
import {listCollections} from "../api/CollectionsAPI";

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

function CollectionsList() {
    const [collections, setCollections] = useState([]);
    const [creating, setCreating] = useState(false);
    const [fetching, setFetching] = useState(true);

    function handleCreateCancelled() {
        setCreating(false);
    }

    function handleCreateCompleted(collectionId) {
        setCreating(false);
        reloadTable();
    }

    function reloadTable() {
        setFetching(true);
        listCollections().then(listResponse => {
            setCollections(listResponse.collections);
            setFetching(false);
        });
    }

    useEffect(() => {
        reloadTable();
    }, []);

    if (fetching) {
        return (<Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
        </Spinner>);
    } else {
        return (
            <div className="speciome-collections-list">
                <CollectionDialog
                    show={creating}
                    onCancel={handleCreateCancelled}
                    onComplete={handleCreateCompleted}
                />
                <CollectionsTable collections={collections}/>
                <Button onClick={() => setCreating(true)}>
                    Create Collection
                </Button>
            </div>
        );
    }
}

export default CollectionsList;