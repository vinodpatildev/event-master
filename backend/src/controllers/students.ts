import { RequestHandler } from "express";
import createHttpError from 'http-errors';
import StudentModel from "../models/student";
import bcrypt from "bcrypt";

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
            password: passwordHashed
        });

        req.session.userSessionId = newStudent._id;

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