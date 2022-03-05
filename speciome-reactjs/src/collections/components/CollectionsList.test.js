import {fireEvent, render, screen, waitFor} from "@testing-library/react";
import {setupServer} from "msw/node";
import {rest} from "msw";

import {BASE_URL} from "../api/CollectionsAPI";
import CollectionsList from "./CollectionsList";
import CollectionStore from "../stores/CollectionStore";

const collections = [
    {
        collectionId: 1,
        collectionName: 'Collection 1'
    },
    {
        collectionId: 2,
        collectionName: 'Collection 2'
    }
]

async function findCreateButton() {
    const createButton = await screen.findByRole('button', {name: 'Create Collection'});
    return {createButton};
}

const server = setupServer();

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

function setupTest(collections) {
    server.use(
        rest.get(`${BASE_URL}/collections`, (req, res, ctx) => {
            const responsePayload = {
                totalCount: collections.length,
                collections: collections
            };
            return res(ctx.status(200), ctx.json(responsePayload));
        })
    );
    collections.forEach(({collectionId}) => {
       server.use(
           rest.delete(`${BASE_URL}/collection/${collectionId}`, ((req, res, ctx) => {
               return res(ctx.status(200), ctx.json({}));
           }))
       )
    });
    const store = new CollectionStore();
    render(<CollectionsList collectionStore={store}/>);
}

describe('Top view rendering without collections', () => {
    beforeEach(() => setupTest([]));

    test('"Create collection" button is displayed', async () => {
        const {createButton} = await findCreateButton();
        expect(createButton).not.toBeNull();
    });

    test('"No collections" message is displayed', async () => {
        const label = await screen.findByText(/You do not have/i);
        expect(label).not.toBeNull();
    });
});

describe('List rendering - two collections present', () => {
    beforeEach(() => setupTest(collections));

    test('"No collections" message is not displayed', async () => {
        const {createButton} = await findCreateButton();
        expect(createButton).not.toBeNull();

        const noCollectionsLabel = screen.queryByText(/You do not have/i);
        expect(noCollectionsLabel).toBeNull();
    });

    test('Both collection names are displayed', async () => {
        const {createButton} = await findCreateButton();
        expect(createButton).not.toBeNull();

        collections.forEach(({collectionName}) => {
            expect(screen.queryByText(collectionName, {exact: true})).not.toBeNull();
        })
    });

    test('"Remove" button is displayed for each collection', async () => {
        await findCreateButton();

        const removeButtons = screen.getAllByRole('button', {name: 'Remove'});
        expect(removeButtons).toHaveLength(2);
    })
});

describe('Interaction tests', () => {
    beforeEach(() => setupTest(collections));

    test('"Create collection" clicked - collection dialog is shown', async () => {
        const {createButton} = await findCreateButton();
        fireEvent.click(createButton);
        expect(screen.queryByRole('button', {name: 'Save'})).not.toBeNull();
    });

    test('One of two collections removed - one left', async () => {
        await findCreateButton(); // Wait for initial rendering

        const removeButtons = screen.getAllByRole('button', {name: 'Remove'});
        fireEvent.click(removeButtons[0]);

        await waitFor(() => expect(screen.getAllByRole('button', {name: 'Remove'})).toHaveLength(1));
    })
});