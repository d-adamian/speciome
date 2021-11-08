import {fireEvent, render, screen} from '@testing-library/react';

import LoginForm from './LoginForm.js'
import {BASE_URL} from "../api/UserAPI";
import {changeEmptyResponseCode, setupEmptyResponseServer} from "../../utils/testUtils";

function setup() {
    const buttonElement = screen.getByRole('button', {name: 'Login'});
    const emailInput = screen.getByRole('textbox', {name: 'Email address'});
    const passwordInput = screen.getByLabelText('Password');
    return {buttonElement, emailInput, passwordInput};
}

describe('Rendering tests', () => {
    beforeEach(() => render(<LoginForm/>));

    test('Renders form title and labels', () => {
        const expectedLabels = ['Log in', 'Email address', 'Password'];
        for (const label of expectedLabels) {
            const element = screen.getByText(label);
            expect(element).toBeInTheDocument();
        }
    });

    test('Renders text boxes for email and password', () => {
        const {emailInput, passwordInput} = setup();
        expect(emailInput).toBeInTheDocument();

        expect(passwordInput).toBeInTheDocument();
    });
});

describe('Input validation tests', () => {
    beforeEach(() => render(<LoginForm/>));

    test('Empty input: login button inactive, "empty" messages displayed', () => {
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

    test('Valid email and password: button active, no messages displayed', () => {
        const {buttonElement, emailInput, passwordInput} = setup();
        fireEvent.change(emailInput, {target: {value: 'aaa@bbb.com'}});
        fireEvent.change(passwordInput, {target: {value: 'some_password'}});

        expect(buttonElement).not.toBeDisabled();
        expect(screen.queryByText('Password is too weak')).toBeNull();
        expect(screen.queryByText('Email address is invalid')).toBeNull();
    });
});

describe('API call test', () => {
    const apiPath = `${BASE_URL}/login`;
    const server = setupEmptyResponseServer(apiPath, 204);

    function fillAndClick() {
        const {buttonElement, emailInput, passwordInput} = setup();
        fireEvent.change(emailInput, {target: {value: 'aaa@bbb.com'}});
        fireEvent.change(passwordInput, {target: {value: 'Str0ng_PasSwOrD'}});
        fireEvent.click(buttonElement);
    }

    test('Valid email and password: callback is called', async () => {
        const loginCallback = () => {
            render(<div>Test passed</div>)
        }
        render(<LoginForm successCallback={loginCallback}/>);

        fillAndClick();
        const successResult = await screen.findByText("Test passed");
        expect(successResult).toBeInTheDocument();
    });

    test('Error response from server: notification is shown, callback not called', async () => {
        const loginCallback = jest.fn();
        render(<LoginForm successCallback={loginCallback}/>);

        changeEmptyResponseCode(server, apiPath, 401);

        fillAndClick();
        const errorMessage = await screen.findByText('Incorrect credentials, please try again');
        expect(errorMessage).toBeInTheDocument();
        expect(loginCallback).not.toBeCalled();
    });
});