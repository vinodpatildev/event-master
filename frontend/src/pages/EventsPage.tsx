import { Container } from "react-bootstrap";
import EventsPageLoggedInView from "../components/EventsPageLoggedInView";
import EventsPageLoggedOutView from "../components/EventsPageLoggedOutView";
import { Admin } from "../models/admin";
import styles from "../styles/EventsPage.module.css";

interface EventsPageProps{
    loggedInAdmin:Admin | null
}

const EventsPage = ({loggedInAdmin}:EventsPageProps) => {
    return (
        <Container className={styles.eventsPage}>
      <>
      {
        loggedInAdmin
        ? <EventsPageLoggedInView />
        : <EventsPageLoggedOutView />
      }
      </>
    </Container> 
    );
}

export default EventsPage;