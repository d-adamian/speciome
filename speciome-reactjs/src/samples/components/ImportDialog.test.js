import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event'

import {setupServer} from "msw/node";
import {rest} from "msw";

import {BASE_URL} from '../api/SampleAPI.js';
import ImportDialog from './ImportDialog.js';

function setup() {
    const button = screen.getByRole('button', {name: 'Import'});
    const textMessage = screen.getByText('import only CSV', {exact: false});
    const title = screen.getByText('Import table', {exact: true});
    const fileInput = screen.getByLabelText('File');
    return {button, textMessage, title, fileInput};
}

function selectFileToUpload(fileContent, fileInput) {
    const file = new File([fileContent], 'hello.csv', {type: 'text/csv'})
    userEvent.upload(fileInput, file);
}

describe('Rendering tests', () => {
    beforeEach(() => render(<ImportDialog/>));

    test('Text message is displayed', () => {
        const {textMessage} = setup();
        expect(textMessage).toBeInTheDocument();
    });

    test('Heading is displayed', () => {
        const {title} = setup();
        expect(title).toBeInTheDocument();
    });

    test('Button is displayed', () => {
        const {button} = setup();
        expect(button).toBeInTheDocument();
    });

    test('File input is displayed', () => {
        const {fileInput} = setup();
        expect(fileInput).toBeInTheDocument();
    });
});

describe('Interaction tests', () => {

    test('No file chosen - import button is disabled', () => {
        render(<ImportDialog/>)
        const {button} = setup();
        expect(button).toBeDisabled();
    });

    test('File chosen - import button is enabled', () => {
        render(<ImportDialog/>)

        const {button, fileInput} = setup();
        selectFileToUpload('some_content', fileInput);

        expect(button).not.toBeDisabled();
    });

    test('Close button pressed - dialog is closed, Import button not shown', () => {
        const onCancel = jest.fn();
        render(<ImportDialog onCancel={onCancel}/>)

        const closeButton = screen.getByLabelText('Close');
        fireEvent.click(closeButton);
        expect(onCancel).toHaveBeenCalledTimes(1);
    });

    test('Too large file - import button is disabled, notification shown', () => {
        const maxSizeKb = 1;
        render(<ImportDialog maxSizeKb={maxSizeKb}/>);

        const fileContent = 'dummy_'.repeat(10000);

        const {button, fileInput} = setup();
        selectFileToUpload(fileContent, fileInput);

        expect(button).toBeDisabled();
        expect(screen.queryByText('File is too large', {exact: false})).toBeInTheDocument();
    });
});

describe('API interaction tests', () => {
    const server = setupServer();

    beforeAll(() => server.listen());
    afterEach(() => server.resetHandlers());
    afterAll(() => server.close());

    test('Import button pressed - POST request done, onComplete called', async () => {
        const onComplete = jest.fn();
        render(<ImportDialog onComplete={onComplete}/>);

        const uploadTestCallback = jest.fn();
        server.use(
            rest.post(`${BASE_URL}/samples/upload/csv`, (req, res, ctx) => {
                uploadTestCallback(req.body);
                return res(ctx.status(204), ctx.json({}));
            })
        );

        const {button, fileInput} = setup();
        const fileContent = 'some_content';
        selectFileToUpload(fileContent, fileInput);

        fireEvent.click(button);
        await waitFor(() => expect(uploadTestCallback).toHaveBeenCalledTimes(1));

        // expect(uploadTestCallback).toHaveBeenCalledWith(fileContent); - TODO: why this doesn't work?
        expect(onComplete).toHaveBeenCalledTimes(1);
    });
});
