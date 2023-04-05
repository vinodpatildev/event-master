import { getAllNotifications } from './../controllers/notifications';
import express from 'express';
import moment from 'moment';
import * as NotificationsController from '../controllers/notifications';
import NotificationModel from '../models/notification';

const router = express.Router();

router.get("/", NotificationsController.getAllNotifications);

router.get("/admin/", NotificationsController.getNotificationByAdmin);

router.get("/:notificationId", NotificationsController.getNotificationsByAdmin);

router.post("/", NotificationsController.createNotificationByAdmin);

router.patch("/:notificationId", NotificationsController.updateNotificationByAdmin);

router.delete("/:notificationId", NotificationsController.deleteNotificationByAdmin);

export default router;