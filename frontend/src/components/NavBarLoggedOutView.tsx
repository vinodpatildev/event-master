import { Button, Navbar } from "react-bootstrap";
import { Admin } from "../models/admin";
import * as EventsApi from "../network/events_api";

interface NavBarLoggedOutViewProps {
    onSignUpClicked: () => void,
    onLoginClicked: () => void,
}

const NavBarLoggedOutView = ({onSignUpClicked, onLoginClicked} : NavBarLoggedOutViewProps) => {
    return (
        <>
            <Button className="btn btn-secondary" onClick={onSignUpClicked} >
                SignUp
            </Button>
            <Button className="btn btn-secondary" onClick={onLoginClicked}>
                Login
            </Button>
        </>
    );
}

export default NavBarLoggedOutView;