import {fireEvent, render, screen, waitFor} from "@testing-library/react";
import {setupServer} from "msw/node";
import {rest} from "msw";

import {BASE_URL} from "../api/CollectionsAPI";
import CollectionsList from "./CollectionsList";
import CollectionStore from "../stores/CollectionStore";

const ownerEmail = "aaa@bbb.com";
const collections = [
    {
        collectionId: 1,
        collectionName: 'Collection 1',
        createdAt: "2022-03-09T06:57:38.452+00:00",
        updatedAt: "2022-03-09T07:18:38.452+00:00",
        ownerEmail: ownerEmail,
        archived: true
    },
    {
        collectionId: 2,
        collectionName: 'Collection 2',
        createdAt: "2022-02-10T06:57:38.452+00:00",
        updatedAt: "2022-02-10T07:57:38.452+00:00",
        ownerEmail: ownerEmail,
        archived: false
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

    test.each(['Remove', 'Archive'])('"%s" button is displayed once', async (label) => {
        await findCreateButton();

        const buttons = screen.getAllByRole('button', {name: label});
        expect(buttons).toHaveLength(1);
    });

    test("Owner email is displayed for each collection", async () => {
        const emails = await screen.findAllByText(ownerEmail);
        expect(emails).toHaveLength(collections.length);
    });

    test("Date is displayed for collection", async() => {
        const expectedDateString = "09/03/2022";
        const date = await screen.findByText(expectedDateString, {exact: false});
        expect(date).not.toBeNull();
    });
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

    test('"Archive" button pushed - collection archived, API endpoint called', async () => {
        const archiveCallback = jest.fn();
        const restoreCallback = jest.fn();

        collections.forEach((collection) => {
            server.use(
                rest.put(`${BASE_URL}/collection/${collection.collectionId}/archive`, (req, res, ctx) => {
                    archiveCallback(collection.collectionId);
                    const updatedCollection = {...collection, archived: true}
                    return res(ctx.status(204), ctx.json(updatedCollection));
                }),
                rest.put(`${BASE_URL}/collection/${collection.collectionId}/unarchive`, (req, res, ctx) => {
                    restoreCallback(collection.collectionId);
                    const updatedCollection = {...collection, archived: false}
                    return res(ctx.status(204), ctx.json(updatedCollection));
                })
            )
        });

        await findCreateButton(); // Wait for initial rendering

        const archiveButton = screen.getByRole('button', {name: 'Archive'});
        fireEvent.click(archiveButton);

        const restoreButton = await screen.findByRole('button', {name: 'Restore'});
        expect(restoreButton).not.toBeNull();

        fireEvent.click(restoreButton);
        await waitFor(() => expect(screen.getAllByRole('button', {name: 'Archive'})).toHaveLength(1));

        expect(archiveCallback).toHaveBeenCalledTimes(1);
        expect(restoreCallback).toHaveBeenCalledTimes(1);
    });
});