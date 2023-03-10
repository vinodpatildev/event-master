import { Button, Navbar } from "react-bootstrap";
import { Admin } from "../models/admin";
import * as EventsApi from "../network/events_api";

interface NavBarLoggedInViewProps {
    admin:Admin,
    onLogoutSuccessful: () => void,
}

const NavBarLoggedInView = ({admin, onLogoutSuccessful} : NavBarLoggedInViewProps) => {
    async function logout() {
        try {
            await EventsApi.logout()
            onLogoutSuccessful();
        }catch(error){
            console.error(error);
            alert(error);
        }
    }
    return (
        <>
            <Navbar.Text className="me-2">Signed in as: {admin.username}</Navbar.Text>
            <Button className="btn btn-secondary" onClick={logout} >
                Logout
            </Button>
        </>
    );
}

export default NavBarLoggedInView;