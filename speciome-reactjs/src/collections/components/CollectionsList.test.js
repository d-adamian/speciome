import {fireEvent, render, screen} from "@testing-library/react";

import CollectionsList from "./CollectionsList";

function findCreateButton() {
    const createButton = screen.queryByRole('button', {name: 'Create Collection'});
    return {createButton};
}

beforeEach(() => render(<CollectionsList/>));

describe('Top view rendering', () => {

    test('"Create collection" button is displayed', () => {
        const {createButton} = findCreateButton();
        expect(createButton).toBeInTheDocument();
    });

    test('"No collections" message is displayed', () => {
        const label = screen.queryByText(/You do not have/i);
        expect(label).toBeInTheDocument();
    });
});

describe('Interaction tests', () => {
    test('"Create collection" clicked - collection dialog is shown', () => {
        const {createButton} = findCreateButton();
        fireEvent.click(createButton);
        expect(screen.queryByRole('button', {name: 'Save'})).not.toBeNull();
    });
});