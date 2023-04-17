import mongoose, {  ObjectId, Schema } from 'mongoose';
import createHttpError from 'http-errors';
import fs from 'fs';
import pdfkit from 'pdfkit';
import { RequestHandler } from "express";
import EventModel, { EventState } from '../models/event';
import StudentModel from '../models/student';
import AdminModel from '../models/admin';
import { assertIsDefined } from '../util/assertIsDefined';
import path from 'path';

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

export const getAllRegisteredEvents: RequestHandler = async (req, res, next) => {
    const studentId = req.params.studentId;
    const authenticatedUserId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedUserId);
        if(!mongoose.isValidObjectId(studentId)){
            throw createHttpError(400,"Invalid event id.")
        }
        const student = await StudentModel.findById(studentId).select("+events_registered").exec();

        const events_registered = student?.events_registered;

        const eventIds = events_registered?.map(eventObject => eventObject.event);

        const events = await EventModel.find({
            _id: { $in: eventIds },
        }).exec();

        res.status(200).json(events);
    }catch(error){
        next(error);
    }
}

export const getAllCreatedEventsByAdmin: RequestHandler = async (req, res, next) => {
    const adminId = req.params.adminId;
    const authenticatedUserId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedUserId);
        if(!mongoose.isValidObjectId(adminId)){
            throw createHttpError(400,"Invalid event id.")
        }
        const admin = await AdminModel.findById(adminId).select("+events_registered").exec();

        const events_created = admin?.events_created;

        const eventIds = events_created?.map(eventObject => eventObject.event);

        const events = await EventModel.find({
            _id: { $in: eventIds },
        }).exec();

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
interface CreateEventParams {
    adminId: string
}
interface CreateEventBody {
    title?: string,
    type?: string,
    description?:string,
    location?:string,
    event_link?: string,
    start?: Date,
    end?: Date,
    department?:string,
    organizer?:string,
    longitude?:string,
    latitude?:string,
}
export const createEventByAdmin: RequestHandler<CreateEventParams, unknown, CreateEventBody, unknown> = async (req, res, next) => {
    const adminId = req.params.adminId;
    const authenticatedAdminId = req.session.userSessionId;
    const title = req.body.title;
    const department = req.body.department;
    const organizer = req.body.organizer;
    const description = req.body.description;
    const type = req.body.type;
    const event_link = req.body.event_link;
    const start = req.body.start;
    const end = req.body.end;
    const location = req.body.location;
    const longitude = req.body.longitude;
    const latitude = req.body.latitude;
    try{
        assertIsDefined(authenticatedAdminId);
        if(!mongoose.isValidObjectId(adminId)){
            throw createHttpError(400, "Invalid admin id. ")
        }
        if(authenticatedAdminId.toString()  != adminId){
            throw createHttpError(401,"You cannot create event on behalf of someone else.");
        }
        if(!title || !department || !organizer || !description || !type || !event_link || !start || !end || !location || !longitude || !latitude){
            throw createHttpError(400,"Parameters missing.");
        }
        
        const admin = await AdminModel.findById(adminId).select("+events_created").exec();
        console.log(admin)
        if(!admin) {
            throw createHttpError(404,"Admin not found")
        }

        const newEvent = await EventModel.create({
            adminId: adminId,
            title:title,
            department:department,
            organizer:organizer,
            description:description,
            type:type,
            event_link:event_link,
            start:start,
            end:end,
            state: EventState.UPCOMING,
            location:location,
            longitude:longitude,
            latitude:latitude
        });

        admin.events_created.push({ event: newEvent._id, present: false });
        console.log(admin)
        await admin.save()
        res.status(200).json(newEvent);
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


export const getEventReportAdmin: RequestHandler = async (req, res, next) => {
    const eventId = req.params.eventId;
    const authenticatedAdminId = req.session.userSessionId;
    try{
        assertIsDefined(authenticatedAdminId);
        if(!mongoose.isValidObjectId(eventId)){
            throw createHttpError(400,"Invalid event id.")
        }
        const event = await EventModel.findById(eventId).select("+attendees").exec();

        if(!event){
            throw createHttpError(404, "Event not found.");
        }
        if(!event.adminId.equals(authenticatedAdminId)){
            throw createHttpError(401,"You cannot access this event.");
        }

        const attendees = event?.attendees;

        const studentIds = attendees?.map(eventObject => eventObject.student);

        const students = await StudentModel.find({
            _id: { $in: studentIds },
        }).exec();

        students.forEach((student) => {
            const attendee = attendees.find(
              (eventObject) => eventObject.student.toString() === student._id.toString()
            );
          
            if (attendee) {
              student.present = attendee.present;
            }
          });

        res.status(200).json(students);
    }catch(error){
        next(error);
    }
}

interface CertificateForEventBody { 
    studentId:string,
    eventId:string,
}

export const getEventCertificate: RequestHandler<unknown, unknown, CertificateForEventBody, unknown> = async(req, res, next) => {
    const authenticatedStudentId = req.session.userSessionId;
    const studentId = req.body.studentId;
    const eventId = req.body.eventId;

    try{
        assertIsDefined(authenticatedStudentId);
        if(!mongoose.isValidObjectId(studentId)){
            throw createHttpError(400, "Invalid student id. ")
        }
        if(authenticatedStudentId.toString()  != studentId){
            throw createHttpError(401,"You cannot download event certificate on behalf of someone else.");
        }
        if( !eventId || !studentId ){
            throw createHttpError(400,"Parameters missing.");
        }
        
        const student = await StudentModel.findById(studentId).select("+events_attended").exec();
        // console.log(student)
        if(!student) {
            throw createHttpError(404,"Student not found")
        }

        const event = await EventModel.findById(eventId).exec();
        // console.log(event)
        if(!event) {
            throw createHttpError(404,"Event not found")
        }
        
        
        const eventObjectId = new mongoose.Types.ObjectId(eventId);
        if (student.events_attended.some((e) => e.event.equals(eventObjectId))) {
            const certificateId = eventId+studentId;
            const participantName = student.name;
            const eventName = event.title;
            const eventDate = new Date(event.start).toLocaleString([], { dateStyle: 'short', timeStyle: 'short' });
            const orgName = student.id
            const location = event.location

            // Create a new PDF document using pdfkit
            const doc = new pdfkit({size: [2000, 1414]});
  
            // Set the document title and metadata
            doc.info['Title'] = `${participantName} - ${eventName} Participation Certificate`;
            doc.info['Author'] = `${orgName}`;
            
            // Set the document background image
            const backgroundImagePath = path.join(__dirname, '../../assets/certback.jpg');
            console.log(backgroundImagePath)
            doc.image(backgroundImagePath, 0, 0, { fit: [doc.page.width, doc.page.height] });

            // Set the font size and line height
            doc.fontSize(18);
            doc.lineGap(32);
            doc.moveDown();
            doc.text(`--- CERTIFICATE OF PARTICIPATION ---`, { align: 'center' });

            doc.font('Helvetica-BoldOblique');
            doc.fontSize(50);
            doc.lineGap(450);
            doc.moveDown();
            doc.lineGap(20);
            doc.text(`${participantName}`, { align: 'center' })
            doc.fontSize(40);
            doc.moveDown();
            doc.lineGap(10);
            doc.text(`${eventName}`, { align: 'center' });
            doc.moveDown();
            doc.text(`held on ${eventDate}`, { align: 'center' });
            doc.moveDown();
            doc.text(`at ${location}`, {align: 'center'});
            doc.lineGap(170);
            doc.fontSize(18);
            doc.moveDown();
            doc.text(`Certificate ID: ${certificateId}`, { align: 'center' });
  
            // Create a unique filename for the PDF and save it to disk
            const filename = `${participantName}-${eventName}-Certificate.pdf`;
            const filePath = `./certificates/${filename}`;
            doc.pipe(fs.createWriteStream(filePath));
            doc.end();
  
            // Return the PDF to the client
            res.setHeader('Content-Type', 'application/pdf');
            res.setHeader('Content-Disposition', `attachment; filename="${filename}"`);
            fs.createReadStream(filePath).pipe(res);
        }else{
            throw createHttpError( 404, `Student with id ${studentId} did not attend event with id ${eventId}.`);
        }
    }catch(error){
        next(error);
    }
}