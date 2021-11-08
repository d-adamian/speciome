import {Container, Nav, Navbar} from "react-bootstrap";
import {Link} from "react-router-dom";
import React from "react";

function SignedOutNav() {
    return (
        <Nav>
            <Nav.Link as={Link} to="/register">Register</Nav.Link>
            <Nav.Link as={Link} to="/login">Login</Nav.Link>
        </Nav>
    );
}

function SignedInNav(props) {
    return (
        <Nav>
            <Navbar.Text>
                Signed in as {props.userEmail}
            </Navbar.Text>
            <Nav.Link onClick={props.logoutCallback}>
                Logout
            </Nav.Link>
        </Nav>
    )
}

function NavigationBar(props) {
    return (
        <Navbar bg="light" expand="lg">
            <Container>
                <Navbar.Brand as={Link} to="/">Speciome</Navbar.Brand>
                {props.userEmail === '' ?
                    <SignedOutNav/> :
                    <SignedInNav userEmail={props.userEmail} logoutCallback={props.logoutCallback}/>
                }
            </Container>
        </Navbar>
    );
}

export default NavigationBar;
