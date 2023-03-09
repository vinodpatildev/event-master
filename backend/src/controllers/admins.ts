import { RequestHandler } from "express";
import createHttpError from 'http-errors';
import AdminModel from "../models/admin";
import bcrypt from "bcrypt";

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