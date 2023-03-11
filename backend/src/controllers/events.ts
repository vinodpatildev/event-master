import mongoose from 'mongoose';
import createHttpError from 'http-errors';
import { RequestHandler } from "express";
import EventModel, { EventState } from '../models/event';
import { assertIsDefined } from '../util/assertIsDefined';

export const getAllEvents: RequestHandler = async (req, res, next) => {
    const authenticatedUserId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedUserId);
        const events = await EventModel.find().exec();
        res.status(200).json(events);
    }catch(error){
        next(error);
    }
}

export const getEventsByAdmin: RequestHandler = async (req, res, next) => {
    const authenticatedAdminId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedAdminId);
        const events = await EventModel.find({adminId:authenticatedAdminId}).exec();
        res.status(200).json(events);
    }catch(error){
        next(error);
    } 
}

export const getEventByAdmin: RequestHandler = async (req, res, next) => {
    const eventId = req.params.eventId;
    const authenticatedAdminId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedAdminId);
        if(!mongoose.isValidObjectId(eventId)){
            throw createHttpError(400,"Invalid event id.")
        }
        const event = await EventModel.findById(eventId).exec();

        if(!event){
            throw createHttpError(404, "Event not found.");
        }
        if(!event.adminId.equals(authenticatedAdminId)){
            throw createHttpError(401,"You cannot access this event.");
        }
        res.status(200).json(event);
    }catch(error){
        next(error);
    }
}
interface CreateEventBody {
    title?: string,
    department?:string,
    organizer?:string,
    description?:string,
    type?: string,
    event_link?: string,
    start?: Date,
    end?: Date,
}
export const createEventByAdmin: RequestHandler<unknown, unknown, CreateEventBody, unknown> = async (req, res, next) => {
    const authenticatedAdminId = req.session.userSessionId;
    const title = req.body.title;
    const department = req.body.department;
    const organizer = req.body.organizer;
    const description = req.body.description;
    const type = req.body.type;
    const event_link = req.body.event_link;
    const start = req.body.start;
    const end = req.body.end;
    try{
        assertIsDefined(authenticatedAdminId);
        if(!title){
            throw createHttpError(400,"Event must have a title.");
        }
        const newEvent = await EventModel.create({
            adminId: authenticatedAdminId,
            title:title,
            department:department,
            organizer:organizer,
            description:description,
            type:type,
            event_link:event_link,
            start:start,
            end:end,
            state: EventState.UPCOMING,
        });
        res.status(201).json(newEvent);
    }catch(error){
        next(error);
    }
}

interface UpdateEventParams {
    eventId: string
}

interface UpdateEventBody{
    title?: string,
    department:string,
    organizer:string,
    description:string,
    type: string,
    event_link: string,
    start: Date,
    end: Date,
}

export const updateEventByAdmin: RequestHandler<UpdateEventParams, unknown, UpdateEventBody, unknown> = async(req, res, next) => {
    const eventId = req.params.eventId;
    const authenticatedAdminId = req.session.userSessionId;
    const newTitle = req.body.title;
    const newDepartment = req.body.department;
    const newOrganizer = req.body.organizer;
    const newDescription = req.body.description;
    const newType = req.body.type;
    const newEvent_link = req.body.event_link;
    const newStart = req.body.start;
    const newEnd = req.body.end;
    try{
        assertIsDefined(authenticatedAdminId);
        if(!mongoose.isValidObjectId(eventId)){
            throw createHttpError(400, "Invalid event id. ")
        }
        if(!newTitle){
            throw createHttpError(400,"Event must have a title.");
        }
        
        const event = await EventModel.findById(eventId).exec();

        if(!event){
            throw createHttpError(404,"Event not found")
        }
        if(!event.adminId.equals(authenticatedAdminId)){
            throw createHttpError(401,"You cannot access this event.");
        }

        event.title = newTitle;
        event.department = newDepartment;
        event.organizer = newOrganizer;
        event.description = newDescription;
        event.type = newType;
        event.event_link = newEvent_link;
        event.start = newStart;
        event.end = newEnd;

        const updatedEvent = await event.save();

        res.status(200).json(updatedEvent);

    }catch(error){
        next(error);
    }
}

export const deleteEventByAdmin: RequestHandler = async(req, res, next) => {
    const eventId = req.params.eventId;
    const authenticatedAdminId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedAdminId);
        if(!mongoose.isValidObjectId(eventId)){
            throw createHttpError(400, "Invalid event id. ")
        }
        const event = await EventModel.findById(eventId).exec();
        if(!event){
            throw createHttpError(404,"event not found")
        }
        if(!event.adminId.equals(authenticatedAdminId)){
            throw createHttpError(401,"You cannot access this event.");
        }
        await event.remove();
        res.sendStatus(204);
    }catch(error){
        next(error);
    }
}