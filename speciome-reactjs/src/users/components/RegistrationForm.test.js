import {fireEvent, render, screen} from '@testing-library/react';

import RegistrationForm from './RegistrationForm.js';
import {BASE_URL} from '../api/UserAPI'
import {changeEmptyResponseCode, setupEmptyResponseServer} from '../../utils/testUtils';

beforeEach(() => render(<RegistrationForm/>));

function setup() {
    const buttonElement = screen.getByRole('button', {name: 'Register'});
    const emailInput = screen.getByRole('textbox', {name: 'Email address'});
    const passwordInput = screen.getByLabelText('Password');
    return {buttonElement, emailInput, passwordInput};
}


describe('Rendering tests', () => {
    test('renders form title and labels', () => {
        const expectedLabels = ['Register new user', 'Email address', 'Password'];
        for (const label of expectedLabels) {
            const element = screen.getByText(label);
            expect(element).toBeInTheDocument();
        }
    });

    test('renders text boxes for email and password', () => {
        const {emailInput, passwordInput} = setup();
        expect(emailInput).toBeInTheDocument();

        expect(passwordInput).toBeInTheDocument();
    });
});

describe('Input validation tests', () => {
    test('Empty input: registration button inactive, "empty" messages displayed', () => {
        const {buttonElement} = setup();
        expect(buttonElement).toBeInTheDocument();
        expect(buttonElement).toBeDisabled();

        expect(screen.queryByText('Email address is empty')).not.toBeNull();
        expect(screen.queryByText('Password is empty')).not.toBeNull();
    });

    test('Invalid e-mail: button inactive, message displayed', () => {
        const {buttonElement, emailInput} = setup();
        fireEvent.change(emailInput, {target: {value: 'NOT_AN_EMAIL'}});

        expect(buttonElement).toBeDisabled();
        const warningElement = screen.queryByText('Email address is invalid');
        expect(warningElement).not.toBeNull();
    });

    test('Too weak password: button inactive, message displayed', () => {
        const {buttonElement, passwordInput} = setup();
        fireEvent.change(passwordInput, {target: {value: '1'}});

        expect(buttonElement).toBeDisabled();
        expect(screen.queryByText('Password is too weak')).not.toBeNull();
    });

    test('Valid email and password: button active, no messages displayed', () => {
        const {buttonElement, emailInput, passwordInput} = setup();
        fireEvent.change(emailInput, {target: {value: 'aaa@bbb.com'}});
        fireEvent.change(passwordInput, {target: {value: 'Str0ng_PasSwOrD'}});

        expect(buttonElement).not.toBeDisabled();
        expect(screen.queryByText('Password is too weak')).toBeNull();
        expect(screen.queryByText('Email address is invalid')).toBeNull();
    });
});

describe('API call test', () => {
    const apiPath = `${BASE_URL}/new-user`;
    const server = setupEmptyResponseServer(apiPath, 204);

    function fillAndClick() {
        const {buttonElement, emailInput, passwordInput} = setup();
        fireEvent.change(emailInput, {target: {value: 'aaa@bbb.com'}});
        fireEvent.change(passwordInput, {target: {value: 'Str0ng_PasSwOrD'}});
        fireEvent.click(buttonElement);
    }

    test('Valid email and password: success response is shown', async () => {
        fillAndClick();
        const regMessage = await screen.findByText('Registration successful');
        expect(regMessage).toBeInTheDocument();
    });

    test('Error response from server: notification is shown', async () => {
        changeEmptyResponseCode(server, apiPath, 400);

        fillAndClick();
        const errorMessage = await screen.findByText('Request unsuccessful, please try again');
        expect(errorMessage).toBeInTheDocument();
    });
});