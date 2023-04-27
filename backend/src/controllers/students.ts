import mongoose from 'mongoose';
import nodemailer from 'nodemailer';
import { RequestHandler } from "express";
import createHttpError from 'http-errors';
import StudentModel from "../models/student";
import EventModel from "../models/event";
import bcrypt from "bcrypt";
import { assertIsDefined } from '../util/assertIsDefined';
import { sendNotificationToAdmin } from '../util/sendNotificationToAdmin';
import { sendNotificationToStudent } from '../util/sendNotificationToStudent';
import { sendNotificationToUser } from '../util/sendNotificationToUser';


export const getAuthenticatedStudent: RequestHandler = async (req, res, next) => {
    try{
        const student = await StudentModel.findById(req.session.userSessionId).select("+email").exec();
        res.status(200).json(student);
    }catch(error){
        next(error);
    }
}

interface SignUpBody {
    username?: string,
    email?: string,
    password?:string,
    name:String,
    registration_no:String,
    dob:String,
    mobile:String,
    year:String,
    department:String,
    division:String,
    passing_year:String,
    profile_image_url:String,
}

export const  signUp : RequestHandler<unknown, unknown, SignUpBody, unknown> = async (req, res, next) => {
    const username = req.body.username;
    const email = req.body.email;
    const passwordRaw = req.body.password;
    const name = req.body.name;
    const registration_no = req.body.registration_no;
    const dob = req.body.dob;
    const mobile = req.body.mobile;
    const year = req.body.year;
    const department = req.body.department;
    const division = req.body.division;
    const passing_year = req.body.passing_year;
    const profile_image_url = req.body.profile_image_url;

    try {
        if(!username || !email || !passwordRaw){
            throw createHttpError(400, "Parameters missing.");
        }

        const existingUsername = await StudentModel.findOne({username:username}).exec();

        if(existingUsername){
            throw createHttpError(409, "Username is already taken. Please choose different one or log in instead.");
        }

        const existingEmail = await StudentModel.findOne({email:email}).exec();

        if(existingEmail){
            throw createHttpError(409, "A user with this email address already exist. Please log in instead.");
        }

        const passwordHashed = await bcrypt.hash(passwordRaw, 10);

        const newStudent  = await StudentModel.create({
            username: username,
            email: email,
            password: passwordHashed,
            name:name,
            registration_no:registration_no,
            dob:dob,
            mobile:mobile,
            year:year,
            department:department,
            division:division,
            passing_year:passing_year,
            profile_image_url:profile_image_url
        });

        req.session.userSessionId = newStudent._id;

        sendNotificationToAdmin("New student registered over EVENT MASTER...",newStudent.name!!)

        res.status(201).json(newStudent);

    } catch(error) {
        next(error);
    }
}

interface LoginBody{
    username?:string,
    password?:string,
}

export const  login : RequestHandler<unknown, unknown, LoginBody, unknown> = async (req, res, next) => {
    const username = req.body.username;
    const passwordRaw = req.body.password;

    try {
        if(!username || !passwordRaw){
            throw createHttpError(400, "Parameters missing.");
        }

        const student = await StudentModel.findOne({username:username}).select("+password +email").exec();

        if(!student){
            throw createHttpError(401, "Invalid credentials.");
        }

        const passwordMatch = await bcrypt.compare(passwordRaw, student.password);

        if(!passwordMatch){
            throw createHttpError(401, "Invalid credentials.");
        }
        req.session.userSessionId = student._id;

        student.password = "******"

        res.status(201).json(student);

    } catch(error) {
        next(error);
    }
}

export const  logout : RequestHandler = (req, res, next) => {
    req.session.destroy(error => {
        if(error){
            next(error);
        }else{
            res.sendStatus(200);
        }
    });
}

interface UpdateStudentParams {
    studentId: string
}

interface UpdateStudentBody{
    registration_no: string,
    name: string,
    dob: string,
    mobile : string,
    year:string,
    department: string,
    division: string,
    passing_year: string,
    profile_image_url:string,
}

export const updateStudent: RequestHandler<UpdateStudentParams, unknown, UpdateStudentBody, unknown> = async(req, res, next) => {
    const studentId = req.params.studentId;
    const authenticatedStudentId = req.session.userSessionId;
    const newRegistrationNo = req.body.registration_no;
    const newName = req.body.name;
    const newDob = req.body.dob;
    const newMobile = req.body.mobile;
    const newYear = req.body.year;
    const newDivision = req.body.division;
    const newPassingYear = req.body.passing_year;
    const new_profile_image_url = req.body.profile_image_url;

    try{
        assertIsDefined(authenticatedStudentId);
        if(!mongoose.isValidObjectId(studentId)){
            throw createHttpError(400, "Invalid student id. ")
        }
        
        const student = await StudentModel.findById(studentId).select("+password +email").exec();

        if(!student){
            throw createHttpError(404,"Student not found")
        }
        if(!student._id.equals(authenticatedStudentId)){
            throw createHttpError(401,"You cannot access this student data.");
        }

        student.registration_no = newRegistrationNo;
        student.name = newName;
        student.dob = newDob;
        student.mobile = newMobile;
        student.year = newYear;
        student.division = newDivision;
        student.passing_year = newPassingYear;
        student.profile_image_url = new_profile_image_url;

        const updatedStudent = await student.save();

        res.status(200).json(updatedStudent);

    }catch(error){
        next(error);
    }

}


interface UpdateStudentPasswordBody{
    old_password: string,
    new_password: string,
}

export const updateStudentPassword: RequestHandler<UpdateStudentParams, unknown, UpdateStudentPasswordBody, unknown> = async(req, res, next) => {
    const studentId = req.params.studentId;
    const authenticatedStudentId = req.session.userSessionId;
    const oldPassword = req.body.old_password;
    const newPassword = req.body.new_password;

    try{
        assertIsDefined(authenticatedStudentId);
        if(!mongoose.isValidObjectId(studentId)){
            throw createHttpError(400, "Invalid student id. ")
        }
        if(!oldPassword || !newPassword){
            throw createHttpError(400, "Parameters missing.");
        }

        const student = await StudentModel.findOne({id:studentId}).select("+password +email").exec();

        if(!student){
            throw createHttpError(404,"Student not found")
        }
        if(!student._id.equals(authenticatedStudentId)){
            throw createHttpError(401,"You cannot access this student data.");
        }

        const passwordMatch = await bcrypt.compare(oldPassword, student.password);

        if(!passwordMatch){
            throw createHttpError(401, "Invalid credentials.");
        }

        const passwordHashed = await bcrypt.hash(newPassword, 10);

        student.password = passwordHashed

        await student.save()

        res.sendStatus(200);

    }catch(error){
        next(error);
    }

}


interface ForgetStudentPasswordBody{
    username: string,
    email: string,
}

export const forgetStudentPassword: RequestHandler<unknown, unknown, ForgetStudentPasswordBody, unknown> = async(req, res, next) => {
    const usernameRaw = req.body.username;
    const emailRaw = req.body.email;

    try{
        
        if(!usernameRaw || !emailRaw){
            throw createHttpError(400, "Parameters missing.");
        }

        const student = await StudentModel.findOne({username:usernameRaw, email:emailRaw}).exec();

        if(!student){
            throw createHttpError(404,"Student with given details not found.")
        }

        // Generate a random OTP and store it in a database or cache
        const otp = Math.floor(100000 + Math.random() * 900000);

        student.otp = otp.toString()

        await student.save()

        
        // Send the OTP to the user's email
        const transporter = nodemailer.createTransport({
            // service:"gmail",
            host: 'smtp.gmail.com',
            port: 587,
            secure: false,
            auth: {
                user: "developer.vinod.patil@gmail.com",
                pass: "xrfnktlflvjsrrli",
            },
        });

        const mailOptions = {
            from: 'developer.vinod.patil@gmail.com',
            to: emailRaw,
            subject: 'Password Reset OTP',
            text: `Your OTP for resetting the password is ${otp}`,
        };
        await transporter.sendMail(mailOptions);

        // Return a success response
        res.sendStatus(200)

    }catch(error){
        next(error);
    }

}


interface ResetStudentPasswordBody{
    email: string,
    password: string,
    otp: string,
}

export const resetStudentPassword: RequestHandler<unknown, unknown, ResetStudentPasswordBody, unknown> = async(req, res, next) => {
    const emailRaw = req.body.email;
    const passwordRaw = req.body.password;
    const otpRaw = req.body.otp;

    try {
        if(!passwordRaw || !otpRaw) {
            throw createHttpError(400, "Parameters missing.");
        }

        const student = await StudentModel.findOne({ email:emailRaw }).select("+otp").exec();

        if(!student){
            throw createHttpError(404,"Student with given details not found.")
        }

        if(otpRaw != student.otp){
            throw createHttpError(400, "Wrong credentials");
        }  

        const passwordHashed = await bcrypt.hash(passwordRaw, 10);
        
        student.otp = ""
        student.password = passwordHashed
    
        await student.save()

        // Return a success response
        res.sendStatus(200)

    }catch(error){
        next(error);
    }
}

interface RegisterStudentForEventBody{
    studentId:string,
    eventId:string,
}

export const registerStudentForEvent: RequestHandler<unknown, unknown, RegisterStudentForEventBody, unknown> = async(req, res, next) => {
    const eventId = req.body.eventId;
    const studentId = req.body.studentId;
    const authenticatedStudentId = req.session.userSessionId

    try {
        if(!eventId || !studentId) {
            throw createHttpError(400, "Parameters missing.");
        }
        assertIsDefined(authenticatedStudentId);
        if(!mongoose.isValidObjectId(eventId)){
            throw createHttpError(400, "Invalid event id. ")
        }
        if(!mongoose.isValidObjectId(studentId)){
            throw createHttpError(400, "Invalid student id. ")
        }

        const student = await StudentModel.findById(studentId);
        if (!student) {
            return res.status(404).json({ message: "Student not found" });
        }

        if(!student._id.equals(authenticatedStudentId)){
            throw createHttpError(401,"You cannot register this for this event.");
        }

        const event = await EventModel.findById(eventId).select("+attendees").exec();
        if (!event) {
            return res.status(404).json({ message: "Event not found" });
        }

        if (event.attendees.some((a) => a.student.toString() === studentId)) {
            return res
              .status(400)
              .json({ message: "Student already registered for this event" });
        }

        event.attendees.push({ student: student._id , present:false});
        await event.save();
        
        if(student.events_registered.some((a) => a.event.toString() == eventId)){
            return res
              .status(400)
              .json({ message: "Student already registered for this event" }); 
        }

        student.events_registered.push({event:event._id});
        await student.save();

        sendNotificationToAdmin("New student registered for an event...",`${student.name} has registered for the event : ${event.title}`)

        res.sendStatus(200)

    }catch(error){
        next(error);
    }
}

interface MarkAttendanceStudentForEventBody{
    studentId:string,
    eventId:string,
}

export const markAttendanceStudentForEvent: RequestHandler<unknown, unknown, MarkAttendanceStudentForEventBody, unknown> = async(req, res, next) => {
    const eventId = req.body.eventId;
    const studentId = req.body.studentId;
    const authenticatedStudentId = req.session.userSessionId

    try {
        if(!eventId || !studentId) {
            throw createHttpError(400, "Parameters missing.");
        }
        assertIsDefined(authenticatedStudentId);
        if(!mongoose.isValidObjectId(eventId)){
            throw createHttpError(400, "Invalid event id. ")
        }
        if(!mongoose.isValidObjectId(studentId)){
            throw createHttpError(400, "Invalid student id. ")
        }

        const student = await StudentModel.findById(studentId);
        if (!student) {
            return res.status(404).json({ message: "Student not found" });
        }

        if(!student._id.equals(authenticatedStudentId)){
            throw createHttpError(401,"You cannot register this for this event.");
        }

        const event = await EventModel.findById(eventId).select("+attendees").exec();
        if (!event) {
            return res.status(404).json({ message: "Event not found" });
        }

        if (event.attendees.some((a) => a.student.toString() === studentId)) {
            const at = event.attendees.find((a) => a.student.toString() === studentId);
            if(at && at.present == true) {
                return res
                        .status(400)
                        .json({ message: "Student already marked present for this event" });
            }
            if(at) at.present = true;
        }

        await event.save();
        
        if(student.events_attended.some((a) => a.event.toString() == eventId)) {
            return res
              .status(400)
              .json({ message: "Student already marked attendance for this event" }); 
        }

        student.events_attended.push({event:event._id});

        await student.save();

        sendNotificationToAdmin("Attendance marked!!",`${student.name} attended event ${event.title}`)

        res.sendStatus(200)

    }catch(error){
        next(error);
    }
}


interface UpdateStudentProfilePictureBody {
    userId: string,
    url: string
}

export const updateStudentProfilePicture: RequestHandler<unknown, unknown, UpdateStudentProfilePictureBody, unknown> = async(req, res, next) => {
    const authenticatedStudentId = req.session.userSessionId;
    const studentId = req.body.userId;
    const url = req.body.url;
    console.log("sessionid: "+authenticatedStudentId)
    console.log("studentId: "+studentId)
    console.log("      url: "+url)


    try{
        assertIsDefined(authenticatedStudentId);
        if(!mongoose.isValidObjectId(studentId)){
            throw createHttpError(400, "Invalid student id. ")
        }
        
        const student = await StudentModel.findById(studentId).exec();

        if(!student){
            throw createHttpError(404,"Student not found")
        }
        if(!student._id.equals(authenticatedStudentId)){
            throw createHttpError(401,"You cannot access this student data.");
        }

        student.profile_image_url = url;

        await student.save();
        
        res.status(200).json(url);

    }catch(error){
        next(error);
    }
}