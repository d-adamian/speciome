import React, {useEffect, useState} from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Redirect
} from "react-router-dom";

import 'bootstrap/dist/css/bootstrap.min.css';

import './App.css';

import {getUserDetails, logout} from "./users/api/UserAPI";

import LoginForm from "./users/components/LoginForm";
import MainPage from "./MainPage";
import NavigationBar from "./NavigationBar";
import RegistrationForm from "./users/components/RegistrationForm";

function App() {
    const [userEmail, setUserEmail] = useState('');

    // Check if already logged in on component load
    useEffect(() => {
        getUserDetails()
            .then(email => setUserEmail(email))
            .catch(() => {
            });
    }, []);

    function handleLogin() {
        getUserDetails().then(email => setUserEmail(email));
    }

    function handleLogout() {
        logout().then(() => setUserEmail(''));
    }

    function WrappedLogin(props) {
        if (props.userEmail === '') {
            return <LoginForm successCallback={handleLogin}/>;
        } else {
            return <Redirect to="/"/>;
        }
    }

    function WrappedRegistrationForm(props) {
        if (props.userEmail === '') {
            return <RegistrationForm/>;
        } else {
            return <Redirect to="/"/>;
        }
    }

    function WrappedMainPage(props) {
        if (props.userEmail === '') {
            return <Redirect to="/login"/>;
        } else {
            return <MainPage/>;
        }
    }

    return (
        <Router>
            <NavigationBar userEmail={userEmail} logoutCallback={handleLogout}/>
            <hr/>
            <Switch>
                <Route exact path="/">
                    <WrappedMainPage userEmail={userEmail}/>
                </Route>
                <Route exact path="/register">
                    <WrappedRegistrationForm userEmail={userEmail}/>
                </Route>
                <Route exact path="/login">
                    <WrappedLogin userEmail={userEmail}/>
                </Route>
            </Switch>
        </Router>
    );
}

export default App;
