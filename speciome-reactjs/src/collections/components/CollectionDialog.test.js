import {fireEvent, render, screen, waitFor} from "@testing-library/react";

import {setupServer} from "msw/node";
import {rest} from "msw";

import CollectionDialog from "./CollectionDialog";
import {BASE_URL} from "../api/CollectionsAPI";

const EXISTING_COLLECTION = {
    collectionId: 1,
    collectionName: 'Existing collection'
};

function findControls() {
    const nameInput = screen.getByRole('textbox', {name: 'Name'});
    const saveButton = screen.queryByRole('button', {name: 'Save'});
    const updateButton = screen.queryByRole('button', {name: 'Update'});
    const warning = screen.queryByText('Collection name is empty');
    return {nameInput, saveButton, warning, updateButton};
}

describe('Dialog rendering for new collection creation', () => {
    beforeEach(() => render(<CollectionDialog/>));

    test('Heading is displayed', () => {
        expect(screen.queryByText('Create new collection')).not.toBeNull();
    });

    test.each(['Save', 'Cancel'])('"%s" button is rendered', (label) => {
        expect(screen.queryByRole('button', {name: label})).not.toBeNull();
    });

    test('Collection name text input is shown', () => {
        expect(screen.getByRole('textbox', {name: 'Name'})).not.toBeNull();
    })
});

describe('Dialog rendering for editing existing collection', () => {
    beforeEach(() => render(<CollectionDialog collection={EXISTING_COLLECTION}/>));

    test('Heading is displayed', () => {
        expect(screen.queryByText('Edit collection')).not.toBeNull();
    });

    test.each(['Update', 'Cancel'])('"%s" button is rendered', (label) => {
        expect(screen.queryByRole('button', {name: label})).not.toBeNull();
    });

    test('Collection name is displayed', () => {
        expect(screen.queryByDisplayValue(EXISTING_COLLECTION.collectionName)).not.toBeNull();
    });
});

describe('Input validation tests', () => {
    beforeEach(() => render(<CollectionDialog/>));

    test('Empty name - "Save" button is disabled, warning is shown', () => {
        const {saveButton, warning} = findControls();
        expect(saveButton).toBeDisabled();
        expect(warning).not.toBeNull();
    });

    test('Non-empty name - "Save" button enabled, warning not shown', () => {
        const {nameInput, saveButton} = findControls();

        fireEvent.change(nameInput, {target: {value: 'Non-empty name'}});
        expect(saveButton).toBeEnabled();

        const {warning} = findControls();
        expect(warning).toBeNull();
    })
});

describe('Interaction tests', () => {
    test('"Close" button pressed - onCancel callback called', () => {
        const onCancel = jest.fn();
        render(<CollectionDialog onCancel={onCancel}/>);

        const closeButton = screen.getByLabelText('Close');
        fireEvent.click(closeButton);
        expect(onCancel).toHaveBeenCalledTimes(1);
    });

    test('"Cancel" button pressed - onCancel callback called', () => {
        const onCancel = jest.fn();
        render(<CollectionDialog onCancel={onCancel}/>);

        const cancelButton = screen.getByRole('button', {name: 'Cancel'});
        fireEvent.click(cancelButton);
        expect(onCancel).toHaveBeenCalledTimes(1);
    });

    describe('API interaction tests', () => {
        const server = setupServer();

        beforeAll(() => server.listen());
        afterEach(() => server.resetHandlers());
        afterAll(() => server.close());

        test('POST API call done on "Save" for new collection', async () => {
            const onComplete = jest.fn();
            render(<CollectionDialog onComplete={onComplete}/>);

            const postCallback = jest.fn();
            const collectionId = 1;
            server.use(
                rest.post(`${BASE_URL}/collection`, (req, res, ctx) => {
                    postCallback();
                    return res(ctx.status(201), ctx.json({collectionId: collectionId}));
                })
            );

            const {nameInput, saveButton} = findControls();

            fireEvent.change(nameInput, {target: {value: 'Non-empty name'}});
            fireEvent.click(saveButton);

            await waitFor(() => expect(postCallback).toHaveBeenCalledTimes(1));

            expect(onComplete).toHaveBeenCalledTimes(1);
            expect(onComplete).toHaveBeenCalledWith(collectionId);
        });

        test('PUT API call done on "Update" for existing collection', async () => {
            const onComplete = jest.fn();
            render(<CollectionDialog collection={EXISTING_COLLECTION} onComplete={onComplete}/>);

            const putCallback = jest.fn();
            const {collectionId} = EXISTING_COLLECTION;
            server.use(
                rest.put(`${BASE_URL}/collection/${collectionId}`, (req, res, ctx) => {
                    putCallback();
                    return res(ctx.status(204), ctx.json({}));
                })
            );

            const {nameInput, updateButton} = findControls();

            fireEvent.change(nameInput, {target: {value: 'Changed name'}});
            fireEvent.click(updateButton);

            await waitFor(() => expect(putCallback).toHaveBeenCalledTimes(1));

            expect(onComplete).toHaveBeenCalledTimes(1);
            expect(onComplete).toHaveBeenCalledWith(collectionId);
        });
    });
});
