import express from 'express';
import * as EventsController from '../controllers/events';

const router = express.Router();

router.get("/", EventsController.getAllEvents);

router.get("/admin/", EventsController.getEventsByAdmin);

router.get("/:eventId",EventsController.getEventByAdmin);

router.post("/",EventsController.createEventByAdmin);

router.patch("/:eventId",EventsController.updateEventByAdmin);

router.delete("/:eventId",EventsController.deleteEventByAdmin);

export default router;