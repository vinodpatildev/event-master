import createHttpError from 'http-errors';
import { RequestHandler } from "express";

export const requiresAuth : RequestHandler = (req, res, next) => {
    if(req.session.userSessionId){
        next();
    }else{
        next(createHttpError(401, "User not authenticated."));
    }
}