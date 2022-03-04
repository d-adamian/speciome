import {fireEvent, render, screen} from "@testing-library/react";
import {setupServer} from "msw/node";
import {rest} from "msw";

import {BASE_URL} from "../api/CollectionsAPI";
import CollectionsList from "./CollectionsList";
import CollectionStore from "../stores/CollectionStore";

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
    const collection1 = {
        collectionName: 'Collection 1'
    };
    const collection2 = {
        collectionName: 'Collection 2'
    }
    beforeEach(() => setupTest([collection1, collection2]));

    test('"No collections" message is not displayed', async () => {
        const {createButton} = await findCreateButton();
        expect(createButton).not.toBeNull();

        const noCollectionsLabel = screen.queryByText(/You do not have/i);
        expect(noCollectionsLabel).toBeNull();
    });

    test('Both collection names are displayed', async () => {
        const {createButton} = await findCreateButton();
        expect(createButton).not.toBeNull();

        [collection1, collection2].forEach(({collectionName}) => {
            expect(screen.queryByText(collectionName, {exact: true})).not.toBeNull();
        })
    });
});

describe('Interaction tests', () => {
    beforeEach(() => setupTest([]));

    test('"Create collection" clicked - collection dialog is shown', async () => {
        const {createButton} = await findCreateButton();
        fireEvent.click(createButton);
        expect(screen.queryByRole('button', {name: 'Save'})).not.toBeNull();
    });
});