import express from 'express';
import * as EventsController from '../controllers/events';
import { sendNotificationToAdmin } from '../util/sendNotificationToAdmin';
import { sendNotificationToStudent } from '../util/sendNotificationToStudent';
import { sendNotificationToUser } from '../util/sendNotificationToUser';
import EventModel, { EventState } from '../models/event';
import moment from 'moment';

const router = express.Router();

router.get("/", EventsController.getAllEvents);

router.get("/registered/:studentId", EventsController.getAllRegisteredEvents);

router.get("/created/:adminId", EventsController.getAllCreatedEventsByAdmin);

router.post("/certificate", EventsController.getEventCertificate);

router.get("/admin", EventsController.getEventsByAdmin);

router.get("/:eventId", EventsController.getEventByAdmin);

router.post("/:adminId", EventsController.createEventByAdmin);

router.patch("/:eventId", EventsController.updateEventByAdmin);

router.delete("/:eventId", EventsController.deleteEventByAdmin);

router.get("/report/:eventId", EventsController.getEventReportAdmin);

async function transitionEvents() {
    try {
        const upcomingEvents = await EventModel.find({ state: EventState.UPCOMING }).exec();
        const liveEvents = await EventModel.find({ state: EventState.LIVE }).exec();
        const finishedEvents = await EventModel.find({ state: EventState.FINISHED }).exec();

        const now = moment();

        for (const event of upcomingEvents) {
            // console.log("processing upcoming a events : " + event.title)
            // console.log(event.start)
            // console.log(now)
            // console.log(moment(event.start).isBefore(now))

            if (moment(event.start).isSameOrBefore(now)) {
                console.log(" success ")
                event.state = EventState.LIVE;
                await event.save();
                sendNotificationToUser("Event is live.....",`The event ${event.title} is live, attend it. Don't forget to mark your attendance using geofence`)
            }
        }

        for (const event of liveEvents) {
            console.log("processing live a events.")
            if (moment(event.end).isSameOrBefore(now)) {
                event.state = EventState.FINISHED;
                await event.save();
                sendNotificationToUser("Event finished ....",`Hope you enjoyed the event ${event.title}. Don't forget to download the certificate in case you attended the event successfully.`)
            }
        }

        // for (const event of finishedEvents) {
        //     console.log("processing finished a events.")
        //     if (moment(event.end).isAfter(now)) {
        //         event.state = EventState.LIVE;
        //         await event.save();
        //     }
        // }
    } catch (err) {
        console.error(`Error transitioning events: ${err}`);
    }
}

setInterval(() => {
    console.log('Transitioning events...');
    transitionEvents();
}, 60 * 1000);


export default router;