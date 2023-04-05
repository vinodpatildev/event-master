import mongoose from 'mongoose';
import { RequestHandler } from "express";
import createHttpError from 'http-errors';
import StudentModel from "../models/student";
import bcrypt from "bcrypt";
import { assertIsDefined } from '../util/assertIsDefined';

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
    passing_year:String
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
            passing_year:passing_year
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
    passing_year: string
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

        const student = await StudentModel.findOne({username:usernameRaw}).select("+password +email").exec();

        if(!student){
            throw createHttpError(404,"Student not found.")
        }

        if(student.email != emailRaw){
            throw createHttpError(401,"This account belongs to someone else.");
        }

        //send email//

        res.sendStatus(200);

    }catch(error){
        next(error);
    }

}


