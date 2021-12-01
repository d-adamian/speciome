import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import {setupServer} from "msw/node";
import {rest} from "msw";

import {BASE_URL} from '../api/SampleAPI.js';
import SamplesTable from './SamplesTable.js';

const columnNames = ['col1_test', 'col2_test'];
const samples = [
    {
        sampleId: 1,
        archive: false,
        attributes: [
            {attribute: 'col1_test', value: 'val_s1_c1'},
            {attribute: 'col2_test', value: 'val_s1_c2'},
        ]
    },
    {
        sampleId: 2,
        archive: false,
        attributes: [
            {attribute: 'col1_test', value: 'val_s2_c1'},
            {attribute: 'col2_test', value: 'val_s2_c2'},
        ]
    },
    {
        sampleId: 3,
        archive: true,
        attributes: [
            {attribute: 'col1_test', value: 'val_s3_c1'},
            {attribute: 'col2_test', value: 'val_s3_c2'},
        ]
    },
];
const numArchived = samples.filter(({archive}) => archive).length;

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

    test.each(['Edit', 'Archive'])('"%s" button is rendered for all non-archived samples', async (label) => {
        const buttons = await screen.findAllByRole('button', {name: label});
        expect(buttons).toHaveLength(samples.length - numArchived);
    });

    test.each(['Restore', 'Delete'])('"%s" button is rendered for all archived samples', async (label) => {
        const buttons = await screen.findAllByRole('button', {name: label});
        expect(buttons).toHaveLength(numArchived);
    });

    test('Given number of rows is displayed', async () => {
        const tableRows = await screen.findAllByRole('row', {exact: true});
        expect(tableRows).toHaveLength(samples.length + 1);
    });

    test('"Export to CSV" button is displayed and has correct link', async () => {
        const exportButton = await screen.findByText('Export to CSV', {exact: true});
        expect(exportButton).toBeInTheDocument();
        // TODO: remove explicit URL to server, should be handled by dev proxy configuration
        expect(exportButton.getAttribute('href')).toBe('http://localhost:8081/samples/download');
    });

    test('"Import" button is displayed once', async () => {
        const importButton = await screen.findByText('Import', {exact: true});
        expect(importButton).toBeInTheDocument();
    });

    test('"Show samples" select is displayed, "All samples" option is selected', async () => {
        const showSamplesSelect = await screen.findByLabelText('Show samples', {exact: true});
        expect(showSamplesSelect).toBeInTheDocument();

        expect(screen.getByText('All samples', {exact: true}).selected).toBeTruthy();
    });
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

        expect(editButtons).toHaveLength(samples.length - numArchived);
        await waitFor(() => expect(editButtons[1]).toBeDisabled());

        const saveButton = await screen.findByRole('button', {name: 'Save'});
        const textInputs = await screen.findAllByRole('textbox', {exact: true});
        expect(textInputs).toHaveLength(columnNames.length);
        fireEvent.change(textInputs[0], {target: {value: 'updated_value'}});
        fireEvent.click(saveButton);

        await waitFor(() => expect(testCallback).toHaveBeenCalledTimes(1));
        await waitFor(() => expect(screen.queryAllByRole('button', {name: 'Save'})).toHaveLength(0));
    });

    test('Archiving row calls API and changes number of "Archive" and "Delete" buttons', async () => {
        const testCallback = jest.fn();
        samples.forEach((sample) => {
            server.use(
                rest.put(`${BASE_URL}/sample/${sample.sampleId}/archive`, (req, res, ctx) => {
                    testCallback(sample.sampleId);
                    const updatedSample = {...sample, archive: true}
                    return res(ctx.status(204), ctx.json(updatedSample));
                })
            )
        });

        const archiveButtons = await screen.findAllByRole('button', {name: 'Archive'});
        fireEvent.click(archiveButtons[0]);
        await waitFor(() => expect(screen.queryAllByRole('button', {name: 'Delete'})).toHaveLength(numArchived + 1));
        await waitFor(
            () => expect(screen.queryAllByRole('button', {name: 'Archive'})
        ).toHaveLength(samples.length - numArchived - 1));

        expect(testCallback).toHaveBeenCalledTimes(1);
    });

    // TODO: merge shared code in tests
    test('Restoring row calls API and changes number of "Archive" and "Delete" buttons', async () => {
        const testCallback = jest.fn();
        samples.forEach((sample) => {
            server.use(
                rest.put(`${BASE_URL}/sample/${sample.sampleId}/unarchive`, (req, res, ctx) => {
                    testCallback(sample.sampleId);
                    const updatedSample = {...sample, archive: false}
                    return res(ctx.status(204), ctx.json(updatedSample));
                })
            )
        });

        const restoreButtons = await screen.findAllByRole('button', {name: 'Restore'});
        fireEvent.click(restoreButtons[0]);
        await waitFor(() => expect(screen.queryAllByRole('button', {name: 'Delete'})).toHaveLength(numArchived - 1));
        await waitFor(
            () => expect(screen.queryAllByRole('button', {name: 'Archive'})
        ).toHaveLength(samples.length - numArchived + 1));

        expect(testCallback).toHaveBeenCalledTimes(1);
    });

    test('Changing archival status option is propagated to API call', async () =>  {
        const testCallback = jest.fn();
        const targetArchivalStatus = 'ARCHIVED';
        server.use(
            rest.get(`${BASE_URL}/samples`, (req, res, ctx) => {
                const archivalStatus = req.url.searchParams.get('archivalStatus');
                testCallback(archivalStatus);
                const response = {
                    totalCount: samples.length,
                    samples: samples
                }
                return res(ctx.status(200), ctx.json(response));
            })
        )
        const showSamplesSelect = await screen.findByLabelText('Show samples', {exact: true});
        expect(showSamplesSelect).toBeInTheDocument();
        userEvent.selectOptions(showSamplesSelect, screen.getByText('Archived only', {exact: true}));

        await waitFor(() => expect(testCallback).toHaveBeenCalledTimes(2));
        expect(testCallback).toHaveBeenCalledWith(targetArchivalStatus);
    });
});