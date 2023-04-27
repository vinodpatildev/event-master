import mongoose from 'mongoose';
import nodemailer from 'nodemailer';
import { RequestHandler } from "express";
import createHttpError from 'http-errors';
import AdminModel from "../models/admin";
import bcrypt from "bcrypt";
import { assertIsDefined } from '../util/assertIsDefined';
import { sendNotificationToAdmin } from '../util/sendNotificationToAdmin';
import { sendNotificationToStudent } from '../util/sendNotificationToStudent';
import { sendNotificationToUser } from '../util/sendNotificationToUser';

export const getAuthenticatedAdmin: RequestHandler = async (req, res, next) => {
    try{
        const admin = await AdminModel.findById(req.session.userSessionId).select("+email").exec();
        res.status(200).json(admin);
    }catch(error){
        next(error);
    }
}

interface SignUpBody {
    username?: string,
    email?: string,
    password?:string
}

export const  signUp : RequestHandler<unknown, unknown, SignUpBody, unknown> = async (req, res, next) => {
    const username = req.body.username;
    const email = req.body.email;
    const passwordRaw = req.body.password;

    try {
        if(!username || !email || !passwordRaw){
            throw createHttpError(400, "Parameters missing.");
        }

        const existingUsername = await AdminModel.findOne({username:username}).exec();

        if(existingUsername){
            throw createHttpError(409, "Username is already taken. Please choose different one or log in instead.");
        }

        const existingEmail = await AdminModel.findOne({email:email}).exec();

        if(existingEmail){
            throw createHttpError(409, "A user with this email address already exist. Please log in instead.");
        }

        const passwordHashed = await bcrypt.hash(passwordRaw, 10);

        const newAdmin  = await AdminModel.create({
            username: username,
            email: email,
            password: passwordHashed
        });

        req.session.userSessionId = newAdmin._id;

        sendNotificationToAdmin("New techer joined EVENT MASTER",`${newAdmin.name} is an administrator now.`)

        res.status(201).json(newAdmin);

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

        const admin = await AdminModel.findOne({username:username}).select("+password +email").exec();

        if(!admin){
            throw createHttpError(401, "Invalid credentials.");
        }

        const passwordMatch = await bcrypt.compare(passwordRaw, admin.password);

        if(!passwordMatch){
            throw createHttpError(401, "Invalid credentials.");
        }
        req.session.userSessionId = admin._id;

        res.status(201).json(admin);

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


interface NotificationBody{
    title:string,
    body:string,
}

export const sendNotification: RequestHandler<unknown, unknown, NotificationBody, unknown> = async(req, res, next) => {
    const titleRaw = req.body.title;
    const bodyRaw = req.body.body;

    try {
        if(!titleRaw || !bodyRaw) {
            throw createHttpError(400, "Parameters missing.");
        }
        sendNotificationToStudent(titleRaw,bodyRaw)
        res.sendStatus(200)
    }catch(error){
        next(error);
    }
}


interface UpdateAdminProfilePictureBody {
    userId: string,
    url: string
}

export const updateAdminProfilePicture: RequestHandler<unknown, unknown, UpdateAdminProfilePictureBody, unknown> = async(req, res, next) => {
    const authenticatedAdminId = req.session.userSessionId;
    const adminId = req.body.userId;
    const url = req.body.url;

    try{
        assertIsDefined(authenticatedAdminId);
        if(!mongoose.isValidObjectId(adminId)){
            throw createHttpError(400, "Invalid admin id. ")
        }
        
        const admin = await AdminModel.findById(adminId).exec();

        if(!admin){
            throw createHttpError(404,"Admin not found")
        }
        if(!admin._id.equals(authenticatedAdminId)){
            throw createHttpError(401,"You cannot access this admin data.");
        }

        admin.profile_image_url = url;

        await admin.save();

        res.status(200).json(url);

    }catch(error){
        next(error);
    }
}

interface ForgetAdminPasswordBody{
    username: string,
    email: string,
}

export const forgetAdminPassword: RequestHandler<unknown, unknown, ForgetAdminPasswordBody, unknown> = async(req, res, next) => {
    const usernameRaw = req.body.username;
    const emailRaw = req.body.email;

    try{
        
        if(!usernameRaw || !emailRaw){
            throw createHttpError(400, "Parameters missing.");
        }

        const admin = await AdminModel.findOne({username:usernameRaw, email:emailRaw}).exec();

        if(!admin){
            throw createHttpError(404,"Admin with given details not found.")
        }

        // Generate a random OTP and store it in a database or cache
        const otp = Math.floor(100000 + Math.random() * 900000);

        admin.otp = otp.toString()

        await admin.save()

        
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

interface ResetAdminPasswordBody{
    email: string,
    password: string,
    otp: string,
}

export const resetAdminPassword: RequestHandler<unknown, unknown, ResetAdminPasswordBody, unknown> = async(req, res, next) => {
    const emailRaw = req.body.email;
    const passwordRaw = req.body.password;
    const otpRaw = req.body.otp;

    try {
        if(!passwordRaw || !otpRaw) {
            throw createHttpError(400, "Parameters missing.");
        }

        const admin = await AdminModel.findOne({ email:emailRaw }).select("+otp").exec();

        if(!admin){
            throw createHttpError(404,"Admin with given details not found.")
        }

        if(otpRaw != admin.otp){
            throw createHttpError(400, "Wrong credentials");
        }  

        const passwordHashed = await bcrypt.hash(passwordRaw, 10);
        
        admin.otp = ""
        admin.password = passwordHashed
    
        await admin.save()

        // Return a success response
        res.sendStatus(200)

    }catch(error){
        next(error);
    }
}