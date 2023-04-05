import mongoose from 'mongoose';
import createHttpError from 'http-errors';
import { RequestHandler } from "express";
import NotificationModel from '../models/notification';
import { assertIsDefined } from '../util/assertIsDefined';

export const getAllNotifications: RequestHandler = async (req, res, next) => {
    const authenticatedUserId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedUserId);
        const notifications = await NotificationModel.find().exec();
        res.status(200).json(notifications);
    }catch(error){
        next(error);
    }
}

export const getNotificationByAdmin: RequestHandler = async (req, res, next) => {
    const authenticatedAdminId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedAdminId);
        const notifications = await NotificationModel.find({adminId:authenticatedAdminId}).exec();
        res.status(200).json(notifications);
    }catch(error){
        next(error);
    } 
}

export const getNotificationsByAdmin: RequestHandler = async (req, res, next) => {
    const notificationId = req.params.notificationId;
    const authenticatedAdminId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedAdminId);
        if(!mongoose.isValidObjectId(notificationId)){
            throw createHttpError(400,"Invalid notification id.")
        }
        const notifications = await NotificationModel.findById(notificationId).exec();

        if(!notifications){
            throw createHttpError(404, "notification not found.");
        }
        if(!notifications.adminId.equals(authenticatedAdminId)){
            throw createHttpError(401,"You cannot access this notification.");
        }
        res.status(200).json(notifications);
    }catch(error){
        next(error);
    }
}
interface CreateNotificationBody {
    title: string;
    body?: string;
    eventId?: string;
    type: string;
    priority?: "high" | "normal" | "low";
    channel?: string;
  }
  export const createNotificationByAdmin: RequestHandler<unknown, unknown, CreateNotificationBody, unknown> = async (req, res, next) => {
    const authenticatedAdminId = req.session.userSessionId;
    const { title, body, eventId, type, priority, channel } = req.body;
    try {
      assertIsDefined(authenticatedAdminId);
      if (!title) {
        throw createHttpError(400, "Notification must have a title.");
      }
      const newNotification = await NotificationModel.create({
        adminId: authenticatedAdminId,
        title,
        body,
        eventId,
        type,
        priority,
        channel,
      });
      res.status(201).json(newNotification);
    } catch (error) {
      next(error);
    }
  };

interface UpdateNotificationParams {
    notificationId: string
}

interface UpdateNotificationBody {
    title?: string;
    body?: string;
    eventId?: string;
    type?: string;
    priority?: "high" | "normal" | "low";
    channel?: string;
  }

export const updateNotificationByAdmin: RequestHandler<UpdateNotificationParams, unknown, UpdateNotificationBody, unknown> = async(req, res, next) => {
    const notificationId = req.params.notificationId;
    const authenticatedAdminId = req.session.userSessionId;
    const newTitle = req.body.title;
    const newBody = req.body.body;
    const newEventId = req.body.eventId;
    const newType = req.body.type;
    const newPriority = req.body.priority;
    const newChannel = req.body.channel;

    try{
        assertIsDefined(authenticatedAdminId);
        if(!mongoose.isValidObjectId(notificationId)){
            throw createHttpError(400, "Invalid notification id. ")
        }
        if(!newTitle){
            throw createHttpError(400,"Notification must have a title.");
        }
        
        const notifications = await NotificationModel.findById(notificationId).exec();

        if(!notifications){
            throw createHttpError(404,"Notification not found")
        }
        if(!notifications.adminId.equals(authenticatedAdminId)){
            throw createHttpError(401,"You cannot access this notification.");
        }

        if(newTitle !== undefined) notifications.title = newTitle;
        if(newBody !== undefined) notifications.body = newBody;
        if(newEventId !== undefined) notifications.eventId = newEventId;
        if(newType !== undefined) notifications.type = newType;
        if(newPriority !== undefined) notifications.priority = newPriority;
        if(newChannel !== undefined) notifications.channel = newChannel;

        const updatedNotification = await notifications.save();

        res.status(200).json(updatedNotification);

    }catch(error){
        next(error);
    }
}

export const deleteNotificationByAdmin: RequestHandler = async(req, res, next) => {
    const notificationId = req.params.notificationId;
    const authenticatedAdminId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedAdminId);
        if(!mongoose.isValidObjectId(notificationId)){
            throw createHttpError(400, "Invalid notification id. ")
        }
        const notifications = await NotificationModel.findById(notificationId).exec();
        if(!notifications){
            throw createHttpError(404,"notification not found")
        }
        if(!notifications.adminId.equals(authenticatedAdminId)){
            throw createHttpError(401,"You cannot access this notification.");
        }
        await notifications.remove();
        res.sendStatus(204);
    }catch(error){
        next(error);
    }
}