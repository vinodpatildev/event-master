import { useEffect, useState } from "react";
import { Button, Col, Row, Spinner } from "react-bootstrap";
import { FaPlus } from "react-icons/fa";
import { Event as EventModel } from "../models/event";
import * as EventsApi from "../network/events_api";
import styleUtils from "../styles/utils.module.css";
import AddEditEventDialog from "./AddEditEventDialog";
import Event from "./Event";
import styles from "../styles/EventsPage.module.css";

const EventsPageLoggedInView = () => {
    const [events, setEvents] = useState<EventModel[]>([]);

    const [eventsLoading, setEventsLoading] = useState(true);
    const [showEventsLoadingError, setShowEventsLoadingError] = useState(false);

    const [showAddEventDialog, setShowAddEventDialog] = useState(false);
    const [eventToEdit, setEventToEdit] = useState<EventModel|null>(null);

    useEffect(() => {
        async function loadEvents() {
          try{
            setShowEventsLoadingError(false);
            setEventsLoading(true);
            const events = await EventsApi.fetchEvents();
            setEvents(events);
          }catch(error){
            console.error(error);
            setShowEventsLoadingError(true);
          }finally{
            setEventsLoading(false);
          }
        }
        loadEvents();    
      },[])
    
      async function deleteEvent(event: EventModel) {
        try {
          await EventsApi.deleteEvent(event._id);
          setEvents(events.filter(existingEvent => existingEvent._id !== event._id));
        }catch(error){
          console.log(error);
          alert(error);
        }
      }

    const eventsGrid = 
  <Row xs={1} md={2} xl={3} className={`g-4 ${styles.eventsGrid}`}>
        {
          events.map( event => ( 
            <Col key={ event._id } >
              <Event
                event={ event } 
                className={ styles.event } 
                onDeleteEventClicked={deleteEvent}
                onEventClicked={setEventToEdit}
              />
            </Col>
           ) )
        }
      </Row>;

    return (
        <>
            <button 
              type="button"
              className={`btn btn-outline-secondary mb-4 ${styleUtils.blockCenter} ${styleUtils.flexCenter}`}
              onClick={() => setShowAddEventDialog(true)}>
                <FaPlus />
                Add new event
            </button>
            {eventsLoading && <Spinner animation='border' variant='primary'/>}
            {showEventsLoadingError && <p>Something went wrong. Please refresh the page.</p>}
            {!eventsLoading && !showEventsLoadingError &&
        <>
          {
            events.length > 0
              ? eventsGrid
              : <p>You have not created any events yet.</p>
          }
        </>
      }
      {
      showAddEventDialog && 
      <AddEditEventDialog 
        onDismiss={() => {setShowAddEventDialog(false);} }
        onEventSaved={(event:EventModel) => {
          setEvents([...events, event]);
          setShowAddEventDialog(false);
        }}
       />
    }
    {
      eventToEdit &&
      <AddEditEventDialog 
        eventToEdit={eventToEdit}
        onDismiss={() => setEventToEdit(null)}
        onEventSaved={(updatedEvent) => {
          setEvents(events.map(existingEvent => existingEvent._id === updatedEvent._id ? updatedEvent : existingEvent))
          setEventToEdit(null);
        }}
      />
    }
        </>
    );
}

export default EventsPageLoggedInView;