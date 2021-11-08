import React, {useState} from 'react';
import Form from 'react-bootstrap/Form';
import Button from "react-bootstrap/Button";


function EditableRow(props) {
    const {columnNames, sample, onCancel, onSave} = props;
    const kvPairs = sample.attributes.map(({attribute, value}) => [attribute, value]);
    const initialAttributeMap = new Map(kvPairs);

    const [attributeMap, setAttributeMap] = useState(initialAttributeMap);

    function updateAttributeValue(name, value) {
        const updatedMap = new Map(attributeMap);
        updatedMap.set(name, value);
        setAttributeMap(updatedMap);
    }

    function handleSave() {
        const attributes = [...attributeMap].map(([attribute, value]) => ({
            attribute: attribute,
            value: value
        }));
        const updatedSample = {
            sampleId: sample.sampleId,
            attributes: attributes
        };
        onSave(updatedSample);
    }

    function isStateDirty() {
        for (const name of columnNames) {
            const initialValue = initialAttributeMap.get(name);
            const currentValue = attributeMap.get(name);
            if (initialValue !== currentValue) {
                return true;
            }
        }
        return false;
    }

    // to avoid recomputing twice
    const dirtyState = isStateDirty();

    return (
        <tr className={dirtyState ? "table-warning" : ""}>
            <td>{sample.sampleId}</td>
            {
                columnNames.map((name, index) => {
                    const value = attributeMap.get(name);
                    return <td key={index}>
                        <Form.Control
                            as="textarea"
                            onChange={event => updateAttributeValue(name, event.target.value)}
                            value={value}
                        />
                    </td>
                })
            }
            <td>
                <Button variant="success" disabled={!dirtyState} onClick={handleSave}>Save</Button>
                <Button variant="danger" onClick={() => onCancel()}>Cancel</Button>
            </td>
        </tr>
    );
}

export default EditableRow;