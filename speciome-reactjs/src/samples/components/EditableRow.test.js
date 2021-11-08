import {fireEvent, render, screen} from '@testing-library/react';
import EditableRow from './EditableRow.js';

const columnNames = ['col1', 'col2', 'col3'];
const sample = {
    sampleId: 11,
    attributes: [
        {attribute: 'col3', value: 'val3'},
        {attribute: 'col1', value: 'val1'},
        {attribute: 'col2', value: 'val2'}
    ]
};

function findButtons() {
    const cancelButton = screen.queryByRole('button', {name: 'Cancel'});
    const saveButton = screen.queryByRole('button', {name: 'Save'});
    return {saveButton, cancelButton};
}

function updateState() {
    const inputElement = screen.getByText('val2', {exact: true});
    fireEvent.change(inputElement, {target: {value: 'val2_updated'}});
}

describe('Initial rendering tests', () => {
    beforeEach(() => render(
        <table>
            <tbody><EditableRow columnNames={columnNames} sample={sample}/></tbody>
        </table>)
    );

    test('Input fields are displayed for all attributes', () => {
        const expectedValues = sample.attributes.map(({value}) => value);
        for (const val of expectedValues) {
            expect(screen.queryByText(val, {exact: true})).toBeInTheDocument();
        }
    });

    test('"Cancel" button is displayed and enabled', () => {
        const {cancelButton} = findButtons();
        expect(cancelButton).toBeInTheDocument();
        expect(cancelButton).toBeEnabled();
    });

    test('Save button is displayed and disabled', () => {
        const {saveButton} = findButtons()
        expect(saveButton).toBeInTheDocument();
        expect(saveButton).toBeDisabled();
    })

    test('Row is not highlighted', () => {
        const row = screen.getByRole('row', {exact: true});
        expect(row).not.toHaveClass('table-warning');
    });
});

describe('Dirty state test', () => {

    beforeEach(() => {
        render(<table>
            <tbody><EditableRow columnNames={columnNames} sample={sample}/></tbody>
        </table>);
        updateState();
    })

    test('Save button is enabled after updating', () => {
        const {saveButton} = findButtons();
        expect(saveButton).toBeEnabled();
    });

    test('Row is highlighted after updating', () => {
        const row = screen.getByRole('row', {exact: true});
        expect(row).toHaveClass('table-warning');
    });
});

describe('Interaction tests', () => {
    const onCancel = jest.fn();
    const onSave = jest.fn();

    beforeEach(() => render(
        <table>
            <tbody>
            <EditableRow columnNames={columnNames} sample={sample} onCancel={onCancel} onSave={onSave}/>
            </tbody>
        </table>
    ));

    test('onCancel callback is called for "Cancel" button', () => {
        const {cancelButton} = findButtons();
        fireEvent.click(cancelButton);

        expect(onCancel).toHaveBeenCalledTimes(1);
    })

    test('onSave callback is called with updated state', () => {
        updateState();
        const {saveButton} = findButtons();
        fireEvent.click(saveButton);

        expect(onSave).toHaveBeenCalledTimes(1);
        const callbackArguments = onSave.mock.calls[0];
        expect(callbackArguments).toHaveLength(1);
        const updatedSample = callbackArguments[0];
        expect(updatedSample.sampleId).toBe(sample.sampleId);

        const expectedAttributesSet = new Set([
            {attribute: 'col3', value: 'val3'},
            {attribute: 'col1', value: 'val1'},
            {attribute: 'col2', value: 'val2_updated'}
        ]);
        const actualAttributesSet = new Set(updatedSample.attributes);
        expect(Array.from(actualAttributesSet)).toStrictEqual(Array.from(expectedAttributesSet));
    });
});