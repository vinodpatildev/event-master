import { Container, Nav, Navbar } from "react-bootstrap";
import NavbarToggle from "react-bootstrap/esm/NavbarToggle";
import { Admin } from "../models/admin";
import NavBarLoggedInView from "./NavBarLoggedInView";
import NavBarLoggedOutView from "./NavBarLoggedOutView";
import { Link } from "react-router-dom";

interface NavBarProps{
    loggedInAdmin: Admin | null,
    onSignUpClicked: () => void,
    onLoginClicked: () => void,
    onLogoutSuccessful: () => void,
}

const NavBar = ({loggedInAdmin, onSignUpClicked, onLoginClicked, onLogoutSuccessful}: NavBarProps) => {
    return (
        <Navbar bg="secondary" variant="dark" expand="sm" sticky="top" >
            <Container>
                <Navbar.Brand as={Link} to="/">
                    Event Master App
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="main-navbar" />
                <Navbar.Collapse id="main-navbar" >
                    <Nav>
                        <Nav.Link as={Link} to="/privacy" > Privacy </Nav.Link> 
                        <Nav.Link as={Link} to="https://vinodpatildev.github.io/"> Developer </Nav.Link>                       
                    </Nav>
                    <Nav className="ms-auto">
                        {
                            loggedInAdmin 
                            ? <NavBarLoggedInView admin={loggedInAdmin} onLogoutSuccessful={onLogoutSuccessful} />
                            : <NavBarLoggedOutView onLoginClicked={onLoginClicked} onSignUpClicked={onSignUpClicked} />
                        }
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
     );
}

export default NavBar;