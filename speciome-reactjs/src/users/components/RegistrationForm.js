import EmailValidator from 'email-validator';
import React, { useState } from 'react';

import Alert from 'react-bootstrap/Alert'
import Button from 'react-bootstrap/Button'
import Form from 'react-bootstrap/Form'

import {registerNewUser} from '../api/UserAPI'

function isPasswordStrong(password) {
    return password.length >= 3;
}

function WarningText(props) {
    return <Form.Text className="text-muted">{props.text}</Form.Text>;
}

function EmailWarning(props) {
    if (props.email === '') {
        return <WarningText text="Email address is empty"/>;
    } else if (!EmailValidator.validate(props.email)) {
        return <WarningText text="Email address is invalid"/>;
    }
    return null;
}

function PasswordWarning(props) {
    if (props.password === '') {
        return <WarningText text="Password is empty"/>;
    } else if (!isPasswordStrong(props.password)) {
        return <WarningText text="Password is too weak"/>;
    }
    return null;
}

function SuccessAlert(props) {
    if (props.show === true) {
        return <Alert variant='success'>Registration successful</Alert>;
    } else {
        return null;
    }
}

function ErrorAlert(props) {
    if (props.show === true) {
        return <Alert variant='danger'>Request unsuccessful, please try again</Alert>
    } else {
        return null;
    }
}

function RegistrationForm() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [completed, setCompleted] = useState(false);
    const [failure, setFailure] = useState(false);

    function handleSubmit(event) {
        event.preventDefault();
        registerNewUser(email, password).then(success => {
            if (success === true) {
                setCompleted(true);
                setFailure(false);
            } else {
                setCompleted(false);
                setFailure(true);
            }
        });
    }

    const submitEnabled = EmailValidator.validate(email) && isPasswordStrong(password);

    return (<Form className="speciome-registration-form">
        <h3>Register new user</h3>
        <SuccessAlert show={completed}/>
        <ErrorAlert show={failure}/>
        <Form.Group controlId="formEmail">
            <Form.Label>Email address</Form.Label>
            <Form.Control
                type="email"
                placeholder="Enter email"
                value={email}
                onChange={event => setEmail(event.target.value)}
            />
            <EmailWarning email={email}/>
        </Form.Group>
        <Form.Group controlId="formPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
                type="password"
                value={password}
                onChange={event => setPassword(event.target.value)}
            />
            <PasswordWarning password={password}/>
        </Form.Group>
        <Button variant="primary" type="button" disabled={!submitEnabled} onClick={handleSubmit}>
            Register
        </Button>
    </Form>);
}

export default RegistrationForm;