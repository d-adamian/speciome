import EmailValidator from 'email-validator';
import React, { useState } from 'react';

import Button from 'react-bootstrap/Button'
import Form from 'react-bootstrap/Form'

import {loginUser} from "../api/UserAPI";
import Alert from "react-bootstrap/Alert";

function WrongCredentialsAlert(props) {
    if (props.show === true) {
        return <Alert variant='danger'>Incorrect credentials, please try again</Alert>
    } else {
        return null;
    }
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
    }
    return null;
}


function LoginForm({successCallback}) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [wrongCredentials, setWrongCredentials] = useState(false);

    const buttonEnabled = EmailValidator.validate(email) && (password !== '');

    function handleSubmit(event) {
        event.preventDefault();
        loginUser(email, password).then(status => {
            if (status === 'success') {
                successCallback();
            } else if (status === 'wrong_credentials') {
                setWrongCredentials(true);
            }
        })
    }

    return (<Form className="speciome-login-form">
        <h3>Log in</h3>
        <WrongCredentialsAlert show={wrongCredentials}/>
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
        <Button variant="primary" type="button" disabled={!buttonEnabled} onClick={handleSubmit}>
            Login
        </Button>
    </Form>);
}

export default LoginForm;