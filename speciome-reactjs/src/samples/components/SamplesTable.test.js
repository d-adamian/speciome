import {fireEvent, render, screen, waitFor} from '@testing-library/react';

import {setupServer} from "msw/node";
import {rest} from "msw";

import {BASE_URL} from '../api/SampleAPI.js';
import SamplesTable from './SamplesTable.js';

const columnNames = ['col1_test', 'col2_test'];
const samples = [
    {
        sampleId: 1,
        attributes: [
            {attribute: 'col1_test', value: 'val_s1_c1'},
            {attribute: 'col2_test', value: 'val_s1_c2'},
        ]
    },
    {
        sampleId: 2,
        attributes: [
            {attribute: 'col1_test', value: 'val_s2_c1'},
            {attribute: 'col2_test', value: 'val_s2_c2'},
        ]
    },
];

const server = setupServer(
    rest.get(`${BASE_URL}/attributes`, (req, res, ctx) => {
        return res(ctx.status(200), ctx.json(columnNames));
    }),
    rest.get(`${BASE_URL}/samples`, (req, res, ctx) => {
        const response = {
            totalCount: samples.length,
            samples: samples
        }
        return res(ctx.status(200), ctx.json(response));
    }),
)

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

describe('Table rendering tests', () => {
    beforeEach(() => render(<SamplesTable/>));

    test('Table header is rendered correctly', async () => {
        for (const name of columnNames) {
            expect(await screen.findByText(name, {exact: true})).toBeInTheDocument();
        }
    });

    test('Sample attributes are rendered in the table', async () => {
        const cellValues = samples.flatMap(smpl => smpl.attributes.map(attr => attr.value));
        for (const cellVal of cellValues) {
            expect(await screen.findByText(cellVal)).toBeInTheDocument();
        }
    });

    test('Edit & Delete buttons are rendered in the table', () => {
        ['Edit', 'Delete'].forEach(async (label) => {
            const buttons = await screen.findAllByRole('button', {name: label});
            expect(buttons).toHaveLength(samples.length);
        });
    })

    test('Given number of rows is displayed', async () => {
        const tableRows = await screen.findAllByRole('row', {exact: true});
        expect(tableRows).toHaveLength(samples.length + 1);
    })
});

describe('Interaction tests', () => {
    beforeEach(() => render(<SamplesTable/>));

    test('"Delete" button removes row and calls API method', async () => {
        const testCallback = jest.fn();
        samples.forEach(({sampleId}) => {
            server.use(
                rest.delete(`${BASE_URL}/sample/${sampleId}`, (req, res, ctx) => {
                    testCallback(sampleId);
                    return res(ctx.status(204), ctx.json({}));
                })
            )
        })
        const deleteButtons = await screen.findAllByRole('button', {name: 'Delete'});
        fireEvent.click(deleteButtons[0])

        await waitFor(() => expect(testCallback).toHaveBeenCalledTimes(1));
        await waitFor(() => expect(screen.queryAllByRole('row', {exact: true})).toHaveLength(samples.length));
    });

    test('"Add Sample" button adds new row and calls API method', async () => {
        const testCallback = jest.fn();
        server.use(
            rest.post(`${BASE_URL}/sample`, (req, res, ctx) => {
                testCallback();
                const sampleId = samples.length + 1;
                return res(ctx.status(201), ctx.json({sampleId: sampleId}));
            })
        );
        const addSampleButton = await screen.findByRole('button', {name: 'Add sample'});
        fireEvent.click(addSampleButton)

        await waitFor(() => expect(testCallback).toHaveBeenCalledTimes(1));
        await waitFor(() =>
            expect(screen.queryAllByRole('row', {exact: true})).toHaveLength(samples.length + 2)
        );
    });

    test('Editing row triggers API call and returns state back to non-editable', async () => {
        const testCallback = jest.fn();
        samples.forEach(({sampleId}) => {
            server.use(
                rest.put(`${BASE_URL}/sample/${sampleId}`, (req, res, ctx) => {
                    testCallback(sampleId);
                    return res(ctx.status(204), ctx.json({}));
                })
            )
        })
        const editButtons = await screen.findAllByRole('button', {name: 'Edit'});
        fireEvent.click(editButtons[0]);

        expect(editButtons).toHaveLength(samples.length);
        await waitFor(() => expect(editButtons[1]).toBeDisabled());

        const saveButton = await screen.findByRole('button', {name: 'Save'});
        const textInputs = await screen.findAllByRole('textbox', {exact: true});
        expect(textInputs).toHaveLength(columnNames.length);
        fireEvent.change(textInputs[0], {target: {value: 'updated_value'}});
        fireEvent.click(saveButton);

        await waitFor(() => expect(testCallback).toHaveBeenCalledTimes(1));
        await waitFor(() => expect(screen.queryAllByRole('button', {name: 'Save'})).toHaveLength(0));
    });
});