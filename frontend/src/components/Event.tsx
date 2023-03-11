import styles from "../styles/Event.module.css";
import styleUtils from "../styles/utils.module.css";
import { Card } from "react-bootstrap";
import { Event as EventModel } from "../models/event";
import { formatDate } from "../utis/formatDate";
import { MdDelete } from "react-icons/md";

interface EventProps {
    event: EventModel,
    className?: string,
    onDeleteEventClicked: (event:EventModel) => void,
    onEventClicked: (event:EventModel) => void,
}

const Event = ( { event, className, onDeleteEventClicked, onEventClicked }:EventProps ) => {
    const {
        title,
        department,
        organizer,
        description,
        type,
        event_link,
        start,
        end,
        createdAt,
        updatedAt
    } = event;

    let createdUpdatedDateText: string;
    if(updatedAt > createdAt) {
        createdUpdatedDateText = "Updated At : " + formatDate(updatedAt);
    }else{
        createdUpdatedDateText = "Created At : " + formatDate(createdAt);
    }
    
    return (
        <Card className={`${styles.eventCard} ${className}`} onClick={() => onEventClicked(event)} >
            <Card.Body className={styles.cardBody}>
                <Card.Title className={styleUtils.flexCenter}> {title}  <MdDelete className="text-muted ms-auto" onClick={(e) => { onDeleteEventClicked(event); e.stopPropagation(); }} /> </Card.Title>
                <Card.Text className={styles.cardText}>  {description} </Card.Text>
            </Card.Body>
            <Card.Footer className="text-muted"> {createdUpdatedDateText} </Card.Footer>
        </Card>
    );
}

export default Event;