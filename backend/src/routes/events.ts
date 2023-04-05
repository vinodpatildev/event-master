import express from 'express';
import moment from 'moment';
import * as EventsController from '../controllers/events';
import EventModel, { EventState } from '../models/event';

const router = express.Router();

router.get("/", EventsController.getAllEvents);

router.get("/admin/", EventsController.getEventsByAdmin);

router.get("/:eventId", EventsController.getEventByAdmin);

router.post("/", EventsController.createEventByAdmin);

router.patch("/:eventId", EventsController.updateEventByAdmin);

router.delete("/:eventId", EventsController.deleteEventByAdmin);

// async function transitionEvents() {
//     try {
//         const upcomingEvents = await EventModel.find({ state: EventState.UPCOMING }).exec();
//         const liveEvents = await EventModel.find({ state: EventState.LIVE }).exec();
//         const finishedEvents = await EventModel.find({ state: EventState.FINISHED }).exec();

//         const now = moment();

//         for (const event of upcomingEvents) {
//             if (moment(event.start).isSameOrBefore(now)) {
//                 event.state = EventState.LIVE;
//                 await event.save();
//             }
//         }

//         for (const event of liveEvents) {
//             if (moment(event.end).isSameOrBefore(now)) {
//                 event.state = EventState.FINISHED;
//                 await event.save();
//             }
//         }

//         for (const event of finishedEvents) {
//             if (moment(event.end).isAfter(now)) {
//                 event.state = EventState.LIVE;
//                 await event.save();
//             }
//         }
//     } catch (err) {
//         console.error(`Error transitioning events: ${err}`);
//     }
// }

// setInterval(() => {
//     console.log('Transitioning events...');
//     transitionEvents();
// }, 60 * 1000);


export default router;